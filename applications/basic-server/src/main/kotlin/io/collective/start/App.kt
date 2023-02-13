package io.collective.start

import freemarker.cache.ClassTemplateLoader
import io.collective.database.devDataSource
import io.collective.entities.Org
import io.collective.entities.OrgIPDataGateway
import io.collective.entities.OrgIPService
import io.collective.ip.WhoIs
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import java.util.*

/**
 * @author dpeterson
 * Main web interface entry point for HTTP get and post.
 */
fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Routing) {
        post("/ip-resolve") {
            val formParameters = call.receiveParameters()
            val ipAddress = formParameters["ip"].toString()
            val ipType = io.collective.ip.IPUtility.validIPAddress(ipAddress)
            if (ipType.equals(io.collective.ip.IPUtility.IPType.IPv4) || ipType.equals(
                    io.collective.ip.IPUtility.IPType.IPv6
                )
            ) {
                val orgIpService = OrgIPService(OrgIPDataGateway(devDataSource()))
                var org = orgIpService.findByIp(ipAddress)
                if (org == null) {
                    org = WhoIs().getOrg(ipAddress)
                    val newOrg: Org? = orgIpService.findByName(org.name)
                    if(newOrg == null)
                            orgIpService.create(org.name, org.orgType)
                }
                call.respond(
                    FreeMarkerContent(
                        "response.ftl", mapOf(
                            "ip" to ipAddress,
                            "errorMessage" to "Valid:  This is a valid IP Address for Organization:  "
                                    + org?.name
                        )
                    )
                )

            } else if (ipType.equals(io.collective.ip.IPUtility.IPType.IPv4Private)) {
                call.respond(
                    FreeMarkerContent(
                        "response.ftl", mapOf(
                            "ip" to ipAddress,
                            "errorMessage" to "Invalid:  Private IP Address entered which can't be part of a public Org"
                        )
                    )
                )
            } else {    // IPType.Neither
                call.respond(
                    FreeMarkerContent(
                        "response.ftl", mapOf(
                            "ip" to ipAddress,
                            "errorMessage" to "Invalid: IP address format not IPv4 or IPv6 format"
                        )
                    )
                )
            }
        }
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("headers" to headers())))
        }
        get("/ip-org-resolve") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("headers" to headers())))
        }
        static("images") { resources("images") }
        static("style") { resources("style") }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.headers(): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    call.request.headers.entries().forEach { entry ->
        headers[entry.key] = entry.value.joinToString()
    }
    return headers
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 80
    embeddedServer(Netty, port, watchPaths = listOf("basic-server"), module = { module() }).start()
}

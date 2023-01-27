package test.collective.start

import io.collective.start.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AppTest {

    @Test
    fun testEmptyHome() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("IP:Org Name Resolver"))
        }
    }

    @Test
    fun testPostPrivateIPAddress() = testApp {
        handleRequest(HttpMethod.Post, "/ip-resolve"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("ip" to "192.168.0.1").formUrlEncode())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("Invalid:  Private"))
        }
    }


    // setBody("ip=192.168.0.1")
    // parametersOf("ip", "192.168.1.1")
    @Test
    fun testPostPrivateInvalidIPAddressFormat() = testApp {
        handleRequest(HttpMethod.Post, "/ip-resolve") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("ip" to "256.168.0.1").formUrlEncode())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("Invalid: IP address"))
        }
    }

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }) { callback() }
    }
}

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

    /**
     * Integration test for valid IP address.  webapp->whois API and back.
     */
    @Test
    fun testPostValidIPAddressFormat() = testApp {
        handleRequest(HttpMethod.Post, "/ip-resolve") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("ip" to "100.8.12.180").formUrlEncode())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("Verizon Business (MCICS)"))
        }
    }

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }) { callback() }
    }
}

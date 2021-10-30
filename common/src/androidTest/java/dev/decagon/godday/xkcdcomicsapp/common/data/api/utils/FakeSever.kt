package dev.decagon.godday.xkcdcomicsapp.common.data.api.utils

import androidx.test.platform.app.InstrumentationRegistry
import dev.decagon.godday.xkcdcomicsapp.common.data.api.ApiConstants
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.IOException
import java.io.InputStream

class FakeServer {
    private val mockWebServer = MockWebServer()

    private val endpointSeparator = "/"
    private val xkcdComicsEndpointPath = endpointSeparator + ApiConstants.XKCD_TEST_ENDPOINT
    private val searchComicsEndpointPath = endpointSeparator + ApiConstants.SEARCH_ENDPOINT
    private val notFoundRequest = MockResponse().setResponseCode(404)

    val baseEndpoint
        get() = mockWebServer.url(endpointSeparator)

    fun start() {
        mockWebServer.start(8080)
    }

    fun setHappyPathDispatcher() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: return notFoundRequest

                return with(path) {
                    when {
                        startsWith(xkcdComicsEndpointPath) -> {
                            MockResponse().setResponseCode(200)
                                .setBody(getJson("xkcdcomics.json"))
                        }
                        startsWith(searchComicsEndpointPath) -> {
                            MockResponse().setResponseCode(200)
                                .setBody(getJson("searchresults.json"))
                        }
                        else -> notFoundRequest
                    }
                }
            }
        }
    }

    fun shutdown() {
        mockWebServer.shutdown()
    }

    private fun getJson(path: String): String {
        return try {
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonStream: InputStream = context.assets.open("networkresponses/$path")
            String(jsonStream.readBytes())
        } catch (exception: IOException) {
            throw exception
        }
    }
}
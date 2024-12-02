package com.cs4090.farmroutes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.ui.order.choose_business.sendObj
import com.cs4090.farmroutes.ui.order.review_order.ReviewOrderViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.util.Calendar


class GetAvailableShoppersTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer: MockWebServer? = null
    private lateinit var orderRepository: OrderRepository
    private var viewModel = ReviewOrderViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockWebServer = MockWebServer()
        try {
            mockWebServer?.start()
        } catch (e: Exception) {
            println("Could not start the MockWebServer: ${e.message}")
        }
        orderRepository = mockk()
        viewModel = ReviewOrderViewModel()
    }

    @After
    fun tearDown() {
        try {
            mockWebServer?.shutdown()
        } catch (e: Exception) {
            println("Failed to shutdown MockWebServer: ${e.message}")
        }
        mockWebServer = null
    }

    @Test
    fun testGetAvailableShoppers() {
        val exampleTimeOne =
            Calendar.getInstance().apply { add(Calendar.HOUR, 72) }.time
        val exampleTimeTwo =
            Calendar.getInstance().apply { add(Calendar.HOUR, 55) }.time
        val exampleTimeThree =
            Calendar.getInstance().apply { add(Calendar.HOUR, 96) }.time

        val expectedResult = listOf(
            OrderTimeSlot(
                shopperID = "11001",
                firstName = "John",
                lastName = "Doe",
                fulfillmentTime = exampleTimeOne
            ), OrderTimeSlot(
                shopperID = "11321",
                firstName = "Alice",
                lastName = "Smith",
                fulfillmentTime = exampleTimeTwo
            ), OrderTimeSlot(
                shopperID = "11001",
                firstName = "John",
                lastName = "Doe",
                fulfillmentTime = exampleTimeThree
            )
        )
        val gson = GsonBuilder().setPrettyPrinting().create()
        val shopperInfoJson = gson.toJson(expectedResult)
        val mockResponseBody = """
        {
            "shoppers": $shopperInfoJson
        }
        """.trimIndent()

        val mockResponse =
            MockResponse().setResponseCode(200).setBody(mockResponseBody)
        mockWebServer!!.enqueue(mockResponse)

        assertNotNull(mockWebServer)

        val serverUrl = mockWebServer!!.url("/getShoppers").toString()
        println("Requesting URL: $serverUrl")
        viewModel.serverUrl = serverUrl

        viewModel.okHttpCLient = OkHttpClient()

        val order = OrderRepository.order
        assertNotNull(order)

        viewModel.ConfirmOrder()

        val recordedRequest = mockWebServer!!.takeRequest()
        assertEquals("POST", recordedRequest!!.method)
        assertEquals("/getShoppers", recordedRequest!!.path)

        val requestBody = recordedRequest!!.body!!.readUtf8()
        val sentData = Gson().fromJson(requestBody, sendObj::class.java)
        println("JSON sent from Frontend: $sentData")

        val actualResult = viewModel.order.value!!.availableShoppers
        assertNotNull(actualResult)
        assertEquals(expectedResult.size, actualResult!!.size)

        expectedResult.zip(actualResult!!).forEach { (expected, actual) ->
            println("EXPECTED:\t$expected")
            println("ACTUAL:  \t$actual\n\n")
            assertEquals(expected.shopperID, actual.shopperID)
            assertEquals(expected.firstName, actual.firstName)
            assertEquals(expected.lastName, actual.lastName)
            assertEquals(
                expected.fulfillmentTime.toString(),
                actual.fulfillmentTime.toString()
            )
        }
    }
}

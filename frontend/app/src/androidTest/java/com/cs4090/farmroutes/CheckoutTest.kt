package com.cs4090.farmroutes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.UPC
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.ui.order.checkout.CheckoutOrderViewModel
import com.cs4090.farmroutes.ui.order.checkout.sendObj
import com.google.gson.Gson
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

class CheckoutTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer: MockWebServer? = null
    private lateinit var orderRepository: OrderRepository
    private var viewModel = CheckoutOrderViewModel()

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
        viewModel = CheckoutOrderViewModel()
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
    fun testConfirmShopper() {
        val mockResponse = MockResponse().setResponseCode(200).setBody("ok")
        mockWebServer!!.enqueue(mockResponse)

        assertNotNull(mockWebServer)

        val serverUrl = mockWebServer!!.url("/checkout").toString()
        println("Requesting URL: $serverUrl")
        viewModel.serverUrl = serverUrl

        viewModel.okHttpCLient = OkHttpClient()

        val order = OrderRepository.order
        assertNotNull(order)

        OrderRepository.addItem(
            OrderItem(
                upc = UPC(10001), name = "Banana", price = 2.50, quantity = 12
            )
        )
        OrderRepository.addItem(
            OrderItem(
                upc = UPC(10003), name = "Milk", price = 3.00, quantity = 1
            )
        )

        viewModel.checkOut()

        val recordedRequest = mockWebServer!!.takeRequest()
        println("REQUEST: $recordedRequest")
        assertEquals("POST", recordedRequest!!.method)
        assertEquals("/checkout", recordedRequest!!.path)

        val requestBody = recordedRequest!!.body!!.readUtf8()
        val sentData = Gson().fromJson(requestBody, sendObj::class.java)
        println("JSON sent from Frontend: $sentData")
        assertEquals(order.value!!.userID, sentData.userID)
        assertEquals(order.value!!.orderID, sentData.orderID)
    }
}

package com.cs4090.farmroutes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.ui.order.schedule_delivery.ScheduleDeliveryViewModel
import com.cs4090.farmroutes.ui.order.schedule_delivery.sendObj
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
import java.util.Calendar


class ConfirmShopperSelectionTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer: MockWebServer? = null
    private lateinit var orderRepository: OrderRepository
    private var viewModel = ScheduleDeliveryViewModel()

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
        viewModel = ScheduleDeliveryViewModel()
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

        val serverUrl = mockWebServer!!.url("/scheduleOrder").toString()
        println("Requesting URL: $serverUrl")
        viewModel.serverUrl = serverUrl

        viewModel.okHttpCLient = OkHttpClient()

        val order = OrderRepository.order
        assertNotNull(order)

        val exampleSelectedTimeSlot = OrderTimeSlot(
            shopperID = "11321",
            firstName = "Alice",
            lastName = "Smith",
            fulfillmentTime = Calendar.getInstance()
                .apply { add(Calendar.HOUR, 55) }.time
        )

        viewModel.updateSelectedTimeSlot(exampleSelectedTimeSlot)

        val recordedRequest = mockWebServer!!.takeRequest()
        println("REQUEST: $recordedRequest")
        assertEquals("POST", recordedRequest!!.method)
        assertEquals("/scheduleOrder", recordedRequest!!.path)

        val requestBody = recordedRequest!!.body!!.readUtf8()
        val sentData = Gson().fromJson(requestBody, sendObj::class.java)
        println("JSON sent from Frontend: $sentData")
        assertEquals(order.value!!.userID, sentData.userID)
        assertEquals(order.value!!.orderID, sentData.orderID)
        assertEquals(exampleSelectedTimeSlot.shopperID, sentData.shopperID)
    }
}

package com.cs4090.farmroutes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.UPC
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.ui.order.choose_business.ChooseBusinessViewModel
import com.cs4090.farmroutes.ui.order.choose_business.itemResponse
import com.cs4090.farmroutes.ui.order.choose_business.sendObj
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

class SelectBusinessTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer: MockWebServer? = null
    private var viewModel = ChooseBusinessViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockWebServer = MockWebServer()
        try {
            mockWebServer?.start()
        } catch (e: Exception) {
            println("Could not start the MockWebServer: ${e.message}")
        }
        viewModel = ChooseBusinessViewModel()
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
    fun testSelectBusiness() {
        val expectedResult = listOf(
            itemResponse(
                upc = 10001, name = "Banana", price = 2.50
            ),
            itemResponse(
                upc = 10002, name = "Bread", price = 1.50
            ),
            itemResponse(
                upc = 10003, name = "Milk", price = 3.00
            ),
            itemResponse(
                upc = 10004, name = "Chicken", price = 6.99
            ),
        )
        val gson = GsonBuilder().setPrettyPrinting().create()
        val itemsInfoJson = gson.toJson(expectedResult)
        val mockResponseBody = """
        {
            "businessID": "1003",
            "items": $itemsInfoJson
        }
        """.trimIndent()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(mockResponseBody)
        mockWebServer!!.enqueue(mockResponse)

        assertNotNull(mockWebServer)

        val serverUrl = mockWebServer!!.url("/selectBusiness").toString()
        println("Requesting URL: $serverUrl")
        viewModel.serverUrl = serverUrl

        viewModel.okHttpCLient = OkHttpClient()

        val exampleSelectedBusiness = BusinessInformation(
            businessID = "1003",
            businessName = "Downtown Market",
            businessAddress = AddressInformation(
                addressPrimary = "789 Pine St",
                addressSecondary = "Building B",
                city = "Kansas City",
                state = USState.KANSAS,
                zipCode = "66101"
            )
        )

        viewModel.updateSelectedBusiness(exampleSelectedBusiness)

        val recordedRequest = mockWebServer!!.takeRequest()
        assertEquals("POST", recordedRequest!!.method)
        assertEquals("/selectBusiness", recordedRequest!!.path)

        val requestBody = recordedRequest!!.body!!.readUtf8()
        val sentData = Gson().fromJson(requestBody, sendObj::class.java)
        println("JSON sent from Frontend: $sentData")

        val actualResult = viewModel.order.value!!.availableItems
        assertNotNull(actualResult)
        assertEquals(expectedResult.size, actualResult!!.size)

        expectedResult.zip(actualResult!!.values)
            .forEach { (expected, actual) ->
                val updatedExpected = OrderItem(
                    upc = UPC(expected.upc),
                    name = expected.name,
                    price = expected.price,
                    quantity = 0
                )
                println("EXPECTED:\t$updatedExpected")
                println("ACTUAL:  \t$actual\n\n")
                assertEquals(updatedExpected.upc, actual.upc)
                assertEquals(updatedExpected.name, actual.name)
                assertEquals(updatedExpected.price, actual.price)
            }
    }
}
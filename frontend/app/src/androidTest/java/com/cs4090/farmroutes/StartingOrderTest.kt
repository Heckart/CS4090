package com.cs4090.farmroutes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.ui.order.select_address.SelectAddressViewModel
import com.cs4090.farmroutes.ui.order.select_address.sendObj
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

class StartingOrderTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer: MockWebServer? = null
    private var viewModel = SelectAddressViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockWebServer = MockWebServer()
        try {
            mockWebServer!!.start()
        } catch (e: Exception) {
            println("Could not start the MockWebServer: ${e.message}")
        }
        viewModel = SelectAddressViewModel()
    }

    @After
    fun tearDown() {
        try {
            mockWebServer!!.shutdown()
        } catch (e: Exception) {
            println("Failed to shutdown MockWebServer: ${e.message}")
        }
        mockWebServer = null
    }

    @Test
    fun testStartOrder() {
        val expectedResult: List<BusinessInformation> = listOf(
            BusinessInformation(
                businessID = "1001",
                businessName = "Fresh Mart",
                businessAddress = AddressInformation(
                    addressPrimary = "123 Oak St",
                    addressSecondary = "Suite A",
                    city = "St. Louis",
                    state = USState.MISSOURI,
                    zipCode = "63101"
                )
            ),
            BusinessInformation(
                businessID = "1002",
                businessName = "Green Grocer",
                businessAddress = AddressInformation(
                    addressPrimary = "456 Elm St",
                    city = "Springfield",
                    state = USState.ILLINOIS,
                    zipCode = "62701"
                )
            ),
            BusinessInformation(
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
        )
        val gson = GsonBuilder().setPrettyPrinting().create()
        val businessInfoJson = gson.toJson(expectedResult)
        val mockResponseBody = """
        {
            "orderID": "123456",
            "businessInfo": $businessInfoJson
        }
        """.trimIndent()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(mockResponseBody)
        mockWebServer!!.enqueue(mockResponse)

        assertNotNull(mockWebServer)

        val serverUrl = mockWebServer!!.url("/startOrder").toString()
        println("Requesting URL: $serverUrl")
        viewModel.serverUrl = serverUrl

        viewModel.okHttpCLient = OkHttpClient()

        viewModel.updatePrimaryAddressInfo("500 W 15th St")
        viewModel.updateCityInfo("Rolla")
        viewModel.updateStateInfo(USState.MISSOURI)
        viewModel.updateZipCodeInfo("65401")

        viewModel.updateOrderAddress()

        val recordedRequest = mockWebServer!!.takeRequest()
        assertEquals("POST", recordedRequest!!.method)
        assertEquals("/startOrder", recordedRequest!!.path)

        val requestBody = recordedRequest!!.body!!.readUtf8()
        val sentData = Gson().fromJson(requestBody, sendObj::class.java)
        println("JSON sent from Frontend: $sentData")

        assertEquals("500 W 15th St", sentData.addressInfo.addressPrimary)
        assertEquals("Rolla", sentData.addressInfo.city)
        assertEquals("65401", sentData.addressInfo.zipCode)

        val actualResult = viewModel.order.value!!.availableBusinesses
        assertNotNull(actualResult)
        assertEquals(expectedResult.size, actualResult!!.size)

        expectedResult.zip(actualResult!!).forEach { (expected, actual) ->
            println("EXPECTED:\t$expected")
            println("ACTUAL:  \t$actual\n\n")
            assertEquals(expected.businessID, actual.businessID)
            assertEquals(expected.businessName, actual.businessName)
            assertEquals(
                expected.businessAddress.addressPrimary,
                actual.businessAddress.addressPrimary
            )
            assertEquals(
                expected.businessAddress.city,
                actual.businessAddress.city
            )
            assertEquals(
                expected.businessAddress.zipCode,
                actual.businessAddress.zipCode
            )
        }
    }
}


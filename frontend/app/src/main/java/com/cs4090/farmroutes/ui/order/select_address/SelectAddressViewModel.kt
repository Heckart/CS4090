package com.cs4090.farmroutes.ui.order.select_address

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.server_url
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient

import okhttp3.RequestBody.Companion.toRequestBody

data class sendObj(
    @SerializedName("userID") val userID: String,
    @SerializedName("addressInfo") val addressInfo: AddressInformation
)

data class responseObj(
    @SerializedName("orderID") val orderID: String,
    @SerializedName("businessInfo") val businessInformation: List<BusinessInformation>,
)

class SelectAddressViewModel() : ViewModel() {
    var okHttpCLient = OkHttpClient()
    var serverUrl: String = server_url + "/startOrder"

    val order = OrderRepository.order

    var primaryAddress by mutableStateOf("")
    var secondaryAddress by mutableStateOf("")
    var city by mutableStateOf("")
    var state by mutableStateOf(USState.ALABAMA)
    var zipCode by mutableStateOf("")

    fun updatePrimaryAddressInfo(input: String) {
        primaryAddress = input
    }

    fun updateSecondaryAddressInfo(input: String) {
        secondaryAddress = input
    }

    fun updateCityInfo(input: String) {
        city = input
    }

    fun updateStateInfo(input: USState) {
        state = input
    }

    fun updateZipCodeInfo(input: String) {
        zipCode = input
    }

    fun updateOrderAddress() {
        val address = AddressInformation(
            addressPrimary = primaryAddress,
            addressSecondary = secondaryAddress,
            city = city,
            state = state,
            zipCode = zipCode
        )
        OrderRepository.updateAddressInformation(address)

        val sendData =
            sendObj(userID = order.value!!.userID, addressInfo = address)
        val json = Gson().toJson(sendData)
        val requestBody = json.toRequestBody()
        val request = okhttp3.Request.Builder()
            .url(serverUrl)
            .post(requestBody)
            .build()
        okHttpCLient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e(
                    "SelectAddressViewModel",
                    "Failed to update selected business: ${response.code}"
                )
                Log.e(
                    "SelectAddressViewModel",
                    "Response body: ${response.body?.string()}"
                )
            } else {
                Log.i(
                    "SelectAddressViewModel",
                    "Order address updated successfully"
                )

                response.body?.let { responseBody ->
                    val responseJson = responseBody.string()
                    Log.i(
                        "SelectAddressViewModel",
                        "Response JSON: $responseJson"
                    )

                    try {
                        val responseObj = Gson().fromJson(
                            responseJson,
                            responseObj::class.java
                        )
                        Log.i(
                            "SelectAddressViewModel",
                            "Response Obj: $responseObj"
                        )
                        OrderRepository.updateAvailableBusinesses(responseObj.businessInformation)
                    } catch (e: Exception) {
                        Log.e(
                            "SelectAddressViewModel",
                            "Error parsing response: ${e.message}"
                        )
                    }
                }
            }
        }
    }
}
package com.cs4090.farmroutes.ui.order.review_order

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody

data class sendObj(
    @SerializedName("userID")
    val userID: String,
    @SerializedName("orderID")
    val orderID: String,
)

data class responseObj(
    @SerializedName("shoppers")
    val shoppers: List<OrderTimeSlot>
)


class ReviewOrderViewModel() : ViewModel() {
    var okHttpCLient = OkHttpClient()
    var serverUrl: String = "http://localhost:8080/getShoppers"

    val order = OrderRepository.order

    fun ConfirmOrder() {
        val sendData = sendObj(
            userID = order.value!!.userID,
            orderID = order.value!!.orderID
        )
        val json = Gson().toJson(sendData)
        val requestBody = json.toRequestBody()
        val request = okhttp3.Request.Builder()
            .url(serverUrl)
            .post(requestBody)
            .build()
        okHttpCLient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e(
                    "ReviewOrderViewModel",
                    "Failed get available shoppers: ${response.code}"
                )
                Log.e(
                    "ReviewOrderViewModel",
                    "Response body: ${response.body?.string()}"
                )
            } else {
                Log.i(
                    "ReviewOrderViewModel",
                    "Order address updated successfully"
                )

                response.body?.let { responseBody ->
                    val responseJson = responseBody.string()
                    Log.i(
                        "ReviewOrderViewModel",
                        "Response JSON: $responseJson"
                    )

                    try {
                        val responseObj = Gson().fromJson(
                            responseJson, responseObj::class.java
                        )
                        OrderRepository.updateAvailableShoppers(responseObj.shoppers)
                    } catch (e: Exception) {
                        Log.e(
                            "ReviewOrderViewModel",
                            "Error parsing response: ${e.message}"
                        )
                    }
                }
            }
        }
    }
}

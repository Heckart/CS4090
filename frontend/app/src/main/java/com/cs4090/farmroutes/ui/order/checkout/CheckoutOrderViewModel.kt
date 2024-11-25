package com.cs4090.farmroutes.ui.order.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.UPC
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody

data class sendItem(
    @SerializedName("upc") val upc: UPC,
    @SerializedName("quantity") val quantity: Int
)

data class sendObj(
    @SerializedName("userID") val userID: String,
    @SerializedName("orderID") val orderID: String,
    @SerializedName("items") val items: List<sendItem>
)

class CheckoutOrderViewModel() : ViewModel() {
    var okHttpCLient = OkHttpClient()
    var serverUrl: String = "http://localhost:8080/checkout"

    val order = OrderRepository.order

    fun checkOut() {
        var sendItems: MutableList<sendItem> = mutableListOf<sendItem>()
        for ((upc, item) in order.value!!.items) {
            sendItems.add(sendItem(upc = upc, quantity = item.quantity))
        }

        val sendData = sendObj(
            userID = order.value!!.userID,
            orderID = order.value!!.orderID,
            items = sendItems
        )
        val json = Gson().toJson(sendData)
        val requestBody = json.toRequestBody()
        val request =
            okhttp3.Request.Builder().url(serverUrl).post(requestBody).build()
        okHttpCLient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e(
                    "SelectAddressViewModel",
                    "Failed to update order address: ${response.code}"
                )
            } else {
                Log.i(
                    "SelectAddressViewModel",
                    "Order address updated successfully"
                )
            }
        }
    }
}
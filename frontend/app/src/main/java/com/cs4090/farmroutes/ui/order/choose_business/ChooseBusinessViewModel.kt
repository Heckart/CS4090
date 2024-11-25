package com.cs4090.farmroutes.ui.order.choose_business

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.UPC
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody

data class itemResponse(
    @SerializedName("upc")
    val upc: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
)

data class responseObj(
    @SerializedName("businessID")
    val businessID: String,
    @SerializedName("items")
    val items: List<itemResponse>
)

data class sendObj(
    val userID: String,
    val orderID: String,
    val businessID: String
)

class ChooseBusinessViewModel : ViewModel() {
    var okHttpCLient = OkHttpClient()
    var serverUrl: String = "http://localhost:8080/selectBusiness"

    val order = OrderRepository.order

    // TODO - Eventually load this in from the DB
    /*
    private val _exampleStores = MutableLiveData<List<BusinessInformation>>(
        listOf(
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
    )
    */

    // TODO - Eventually get _exampleStores from DB
    val availableBusinesses: LiveData<List<BusinessInformation>> =
        order.value?.availableBusinesses?.let {
            MutableLiveData(it)
        } ?: MutableLiveData(emptyList())


    fun updateSelectedBusiness(selectedBusiness: BusinessInformation) {
        OrderRepository.updateBusinessInformation(selectedBusiness)

        val sendData = sendObj(
            userID = order.value!!.userID,
            orderID = order.value!!.orderID,
            businessID = selectedBusiness.businessID
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
                    "ChooseBusinessViewModel",
                    "Failed to update selected business: ${response.code}"
                )
                Log.e(
                    "ChooseBusinessViewModel",
                    "Response body: ${response.body?.string()}"
                )
            } else {
                Log.i(
                    "ChooseBusinessViewModel",
                    "Selected business updated successfully"
                )

                response.body?.let { responseBody ->
                    val responseJson = responseBody.string()
                    Log.i(
                        "ChooseBusinessViewModel",
                        "Response JSON: $responseJson"
                    )

                    try {
                        val responseObj = Gson().fromJson(
                            responseJson,
                            responseObj::class.java
                        )

                        if (responseObj.items.isNullOrEmpty()) {
                            Log.e(
                                "ChooseBusinessViewModel",
                                "No items found in the response"
                            )
                        } else {
                            // Update the available items with the values from
                            // the backend.
                            OrderRepository.setAvailableItems(
                                responseObj.items.map { item ->
                                    OrderItem(
                                        upc = UPC(item.upc),
                                        name = item.name,
                                        price = item.price,
                                        quantity = 0
                                    )
                                }.associateBy { it.upc }
                            )
                            Log.i(
                                "ChooseBusinessViewModel",
                                "Items successfully mapped and added"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "ChooseBusinessViewModel",
                            "Error parsing response: ${e.message}"
                        )
                    }
                } ?: run {
                    Log.e("ChooseBusinessViewModel", "Response body is null")
                }
            }
        }
    }

}
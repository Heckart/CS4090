package com.cs4090.farmroutes.ui.order.schedule_delivery

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.server_url
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody

data class sendObj(
    @SerializedName("userID") val userID: String,
    @SerializedName("orderID") val orderID: String,
    @SerializedName("shopperID") val shopperID: String
)

class ScheduleDeliveryViewModel : ViewModel() {
    var okHttpCLient = OkHttpClient()
    var serverUrl: String = server_url + "/scheduleOrder"

    val order = OrderRepository.order

//    val exampleTimeOne =
//        Calendar.getInstance().apply { add(Calendar.HOUR, 72) }.time
//    val exampleTimeTwo =
//        Calendar.getInstance().apply { add(Calendar.HOUR, 55) }.time
//    val exampleTimeThree =
//        Calendar.getInstance().apply { add(Calendar.HOUR, 96) }.time


    // TODO - Eventually load this in from the DB
//    private val _exampleTimeSlots = MutableLiveData<List<OrderTimeSlot>>(
//        listOf(
//            OrderTimeSlot(
//                shopperID = "11001", firstName = "John", lastName = "Doe",
//                fulfillmentTime = exampleTimeOne
//            ), OrderTimeSlot(
//                shopperID = "11321", firstName = "Alice", lastName = "Smith",
//                fulfillmentTime = exampleTimeTwo
//            ), OrderTimeSlot(
//                shopperID = "11001", firstName = "John", lastName = "Doe",
//                fulfillmentTime = exampleTimeThree
//            )
//        )
//    )
    private val _exampleTimeSlots =
        mutableStateOf(order.value?.availableShoppers)

    // TODO - Eventually get _exampleTimeSlots from DB
    val availableTimeSlots: List<OrderTimeSlot>? get() = _exampleTimeSlots.value

    fun updateSelectedTimeSlot(selectedTimeSlot: OrderTimeSlot) {
        OrderRepository.updateTimeSlot(selectedTimeSlot)

        val sendData = sendObj(
            userID = order.value!!.userID,
            orderID = order.value!!.orderID,
            shopperID = order.value!!.timeSlot!!.shopperID
        )
        val json = Gson().toJson(sendData)
        val requestBody = json.toRequestBody()
        val request =
            okhttp3.Request.Builder().url(serverUrl).post(requestBody).build()
        okHttpCLient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e(
                    "ScheduleDeliveryViewModel",
                    "Failed to schedule order: ${response.code}"
                )
                Log.e(
                    "ScheduleDeliveryViewModel",
                    "Response body: ${response.body?.string()}"
                )
            } else {
                Log.i(
                    "ScheduleDeliveryViewModel", "Order scheduled successfully"
                )
                Log.i("ScheduleDeliveryViewModel", "Request $request")
                Log.i("ScheduleDeliveryViewModel", "Request Body $json")
            }
        }
    }

}

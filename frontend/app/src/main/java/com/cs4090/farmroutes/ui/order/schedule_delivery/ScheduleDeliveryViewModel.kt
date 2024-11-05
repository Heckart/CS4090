package com.cs4090.farmroutes.ui.order.schedule_delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.data.models.Shopper
import com.cs4090.farmroutes.data.repository.OrderRepository
import java.util.Calendar

class ScheduleDeliveryViewModel : ViewModel() {
    val order = OrderRepository.order

    val exampleTimeOne = Calendar.getInstance().apply { add(Calendar.HOUR, 72) }.time
    val exampleTimeTwo = Calendar.getInstance().apply { add(Calendar.HOUR, 55) }.time
    val exampleTimeThree = Calendar.getInstance().apply { add(Calendar.HOUR, 96) }.time


    // TODO - Eventually load this in from the DB
    private val _exampleTimeSlots = MutableLiveData<List<OrderTimeSlot>>(
        listOf(
            OrderTimeSlot(
                shopper = Shopper(
                    shopperID = "11001", firstName = "John", lastName = "Doe"
                ),
                orderTime = exampleTimeOne
            ),
            OrderTimeSlot(
                shopper = Shopper(
                    shopperID = "11321", firstName = "Alice", lastName = "Smith"
                ),
                orderTime = exampleTimeTwo
            ),
            OrderTimeSlot(
                shopper = Shopper(
                    shopperID = "11001", firstName = "John", lastName = "Doe"
                ),
                orderTime = exampleTimeThree
            )
        )
    )

    // TODO - Eventually get _exampleTimeSlots from DB
    val availableTimeSlots: LiveData<List<OrderTimeSlot>> = _exampleTimeSlots

    fun updateSelectedTimeSlot(selectedTimeSlot: OrderTimeSlot) {
        OrderRepository.updateTimeSlot(selectedTimeSlot)
    }

}

package com.cs4090.farmroutes.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.Order
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.OrderStatus
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.data.models.UPC

object OrderRepository {
    private val _order = MutableLiveData<Order>(
        Order(
            orderID = "001", // TODO - Generate automatically
            userID = "002",// TODO - Generate automatically
            deliveryAddress = null,
            businessInfo = null,
            status = OrderStatus.ORDERING,
            items = mutableMapOf<UPC, OrderItem>(),
            timeSlot = null,
        )
    )
    val order: LiveData<Order> get() = _order

    fun updateAddressInformation(address: AddressInformation) {
        // TODO - Add validation logic for address input
        _order.value = _order.value!!.copy(deliveryAddress = address)
    }

    fun updateBusinessInformation(businessInfo: BusinessInformation) {
        // TODO - Add validation logic for business info input
        _order.value = _order.value!!.copy(businessInfo = businessInfo)
    }

    fun addItem(item: OrderItem) {
        val currentItems = _order.value!!.items.toMutableMap()

        if (currentItems.containsKey(item.upc)) /* item is in cart, increment quantity */ {
            val existingItem = currentItems[item.upc]!!
            existingItem.quantity += 1
            currentItems[item.upc] = existingItem
        } else /* item not in cart, add one of item to cart */ {
            currentItems[item.upc] = item.copy(quantity = 1)
        }
        _order.value = _order.value!!.copy(items = currentItems)
    }

    fun removeItem(item: OrderItem) {
        val currentItems = _order.value!!.items.toMutableMap()
        if (currentItems.containsKey(item.upc)) {
            val existingItem = currentItems[item.upc]!!
            if (existingItem.quantity > 1) /* item count is greater than one, remove one item */ {
                existingItem.quantity -= 1
                currentItems[item.upc] = existingItem
            } else /* item count is one, remove from cart */ {
                currentItems.remove(item.upc)
            }
        }
        _order.value = _order.value!!.copy(items = currentItems)
    }

    fun clearItems() {
        _order.value = _order.value!!.copy(items = emptyMap())
    }

    fun updateStatus(status: OrderStatus) {
        _order.value = _order.value!!.copy(status = status)
    }

    fun updateTimeSlot(timeSlot: OrderTimeSlot) {
        _order.value = _order.value!!.copy(timeSlot = timeSlot)
    }

    fun removeTimeSlot(timeSlot: OrderTimeSlot) {
        _order.value = _order.value!!.copy(timeSlot = null)
    }
}
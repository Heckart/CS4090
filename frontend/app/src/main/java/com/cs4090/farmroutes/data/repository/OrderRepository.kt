package com.cs4090.farmroutes.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.Order
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.OrderStatus
import com.cs4090.farmroutes.data.models.UPC
import kotlin.collections.mapOf

object OrderRepository {
    private val _order = MutableLiveData<Order>(
        Order(
            orderID = "001", // TODO - Generate automatically
            userID = "002",// TODO - Generate automatically
            deliveryAddress = null,
            businessInfo = null,
            status = OrderStatus.ORDERING,
            items = mapOf<UPC, OrderItem>(),
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
        currentItems[item.upc] = item
        _order.value = _order.value!!.copy(items = currentItems)
    }

    fun removeItem(upc: UPC) {
        val currentItems = _order.value!!.items.toMutableMap()
        currentItems.remove(upc)
        _order.value = _order.value!!.copy(items = currentItems)
    }

    fun updateItem(item: OrderItem) {
        val currentItems = _order.value!!.items.toMutableMap()
        currentItems[item.upc] = item
        _order.value = _order.value!!.copy(items = currentItems)
    }

    fun clearItems() {
        _order.value = _order.value!!.copy(items = emptyMap())
    }

    fun updateStatus(status: OrderStatus) {
        _order.value = _order.value!!.copy(status = status)
    }
}
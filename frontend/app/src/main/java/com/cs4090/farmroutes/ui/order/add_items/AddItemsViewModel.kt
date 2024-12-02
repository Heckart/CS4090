package com.cs4090.farmroutes.ui.order.add_items

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.UPC
import com.cs4090.farmroutes.data.repository.OrderRepository


class AddItemsViewModel : ViewModel() {
    val order = OrderRepository.order

    // TODO - Eventually load this in from the DB
//    private val _exampleItems = mutableStateOf(
//        mapOf(
//            UPC(10001) to OrderItem(
//                upc = UPC(10001), name = "Banana", price = 2.50, quantity = 0
//            ),
//            UPC(10002) to OrderItem(
//                upc = UPC(10002), name = "Bread", price = 1.50, quantity = 0
//            ),
//            UPC(10003) to OrderItem(
//                upc = UPC(10003), name = "Milk", price = 3.00, quantity = 0
//            ),
//            UPC(10004) to OrderItem(
//                upc = UPC(10004), name = "Chicken", price = 6.99, quantity = 0
//            ),
//        )
//    )
    private val _exampleItems = mutableStateOf(order.value?.availableItems)

    init {
        order.value?.items?.forEach { (upc: UPC, item: OrderItem) ->
            _exampleItems.value?.get(upc)?.quantity = item.quantity
        }
    }

    val availableItems: Map<UPC, OrderItem>? get() = _exampleItems.value  // TODO - eventually get _exampleStores from DB

    fun addItemToCart(upc: UPC) {
        val item = _exampleItems.value?.get(upc)
        if (item != null) {
            OrderRepository.addItem(item)

            val updatedItems = _exampleItems.value?.toMutableMap()
            updatedItems?.set(upc, item.copy(quantity = item.quantity + 1))
            _exampleItems.value = updatedItems
        }
    }

    fun removeItemFromCart(upc: UPC) {
        val item = _exampleItems.value?.get(upc)
        if (item != null) {
            OrderRepository.removeItem(item)

            if (item.quantity >= 1) {
                val updatedItems =
                    (_exampleItems.value as Map<out UPC, OrderItem>).toMutableMap()
                updatedItems[upc] = item.copy(quantity = item.quantity - 1)
                _exampleItems.value = updatedItems
            }
        }
    }
}
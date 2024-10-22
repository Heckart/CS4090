package com.cs4090.farmroutes.ui.order.add_items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.data.models.UPC


class AddItemsViewModel : ViewModel() {

    // TODO - Eventually load this in from the DB
    private val _exampleItems = MutableLiveData<List<OrderItem>>(
        listOf(
            OrderItem(
                upc = UPC(10001),
                name = "Banana",
                price = 2.50,
                quantity = 0
            ),
            OrderItem(
                upc = UPC(10002),
                name = "Bread",
                price = 1.50,
                quantity = 0
            ),
            OrderItem(
                upc = UPC(10003),
                name = "Milk",
                price = 3.00,
                quantity = 0
            ),
            OrderItem(
                upc = UPC(10004),
                name = "Chicken",
                price = 6.99,
                quantity = 0
            ),
        )
    )

    val availableItems: LiveData<List<OrderItem>> = _exampleItems
    // TODO - eventually get _exampleStores from DB

}
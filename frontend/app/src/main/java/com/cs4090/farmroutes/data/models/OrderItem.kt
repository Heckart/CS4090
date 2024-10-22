package com.cs4090.farmroutes.data.models

/**
 * A representation of an item in an order
 *
 * @property upc The universal product code of the item.
 * @property name The name of the item as a string.
 * @property price The price value of the item.
 */
data class OrderItem(
    var upc: UPC,
    var name: String,
    var price: Double,
    var quantity: Int = 0
)


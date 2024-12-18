package com.cs4090.farmroutes.data.models

/**
 * A collection of information about an order.
 *
 * @property orderID The string used to uniquely identify and order.
 * @property userID The string used to uniquely identify the user who placed the order.
 * @property deliveryAddress The address information of where the delivery is to.
 * @property status A one-word description of the order's current status.
 * @property items A list of the items that make up the order.
 * @property timeSlot The timeslot the order when the order will be fulfilled, includes shopper info.
 */
data class Order(
    var orderID: String,
    var userID: String,
    var deliveryAddress: AddressInformation? = null,
    var businessInfo: BusinessInformation? = null,
    var status: OrderStatus,
    var items: Map<UPC, OrderItem>, // TODO - Maybe rename to cart?
    var timeSlot: OrderTimeSlot? = null,
    //
    val availableBusinesses: List<BusinessInformation>? = emptyList(),
    val availableItems: Map<UPC, OrderItem>? = emptyMap(),
    val availableShoppers: List<OrderTimeSlot>? = emptyList()
)
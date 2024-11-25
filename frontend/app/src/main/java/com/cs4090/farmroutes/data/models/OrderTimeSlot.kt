package com.cs4090.farmroutes.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * A representation of an order's time slot from the perspective of a user.
 *
 * @property shopperID The string used to uniquely identify the shopper.
 * @property firstName The first name of the shopper.
 * @property lastName The last name of the shopper
 * @property fulfillmentTime The time the shopper will fulfill the order.
 */
data class OrderTimeSlot(
    @SerializedName("shopperID")
    var shopperID: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("fulfillmentTime")
    var fulfillmentTime: Date,
    // TODO ~ Add more attributes as needed
)

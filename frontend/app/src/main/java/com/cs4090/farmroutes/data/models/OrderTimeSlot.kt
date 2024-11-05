package com.cs4090.farmroutes.data.models

import java.util.Date

/**
 * A representation of an order's time slot from the perspective of a user.
 *
 * @property shopper Information about the shopper that the user should know.
 * @property orderTime The time the order is scheduled to occur.
 */
data class OrderTimeSlot(
    var shopper: Shopper,
    var orderTime: Date,
    // TODO ~ Add more attributes as needed
)

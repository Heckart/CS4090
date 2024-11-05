package com.cs4090.farmroutes.data.models

/**
 * Enumerator for the different possible statuses of an order.
 */
enum class OrderStatus {
    ORDERING,
    CONFIRMED,
    DISPATCHED,
    DELIVERED,
    CANCELED
}
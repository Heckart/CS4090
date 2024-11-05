package com.cs4090.farmroutes.ui.order.review_order

import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.repository.OrderRepository


class ReviewOrderViewModel() : ViewModel() {
    val order = OrderRepository.order
}

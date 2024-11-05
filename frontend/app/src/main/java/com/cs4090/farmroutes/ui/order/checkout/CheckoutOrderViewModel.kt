package com.cs4090.farmroutes.ui.order.checkout

import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.repository.OrderRepository


class CheckoutOrderViewModel() : ViewModel() {
    val order = OrderRepository.order
}
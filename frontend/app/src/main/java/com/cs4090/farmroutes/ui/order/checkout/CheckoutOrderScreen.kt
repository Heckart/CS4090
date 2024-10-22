package com.cs4090.farmroutes.ui.order.checkout

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.ui.order.checkout.CheckoutOrderViewModel

@Composable
fun CheckoutOrderScreen(
    viewModel: CheckoutOrderViewModel = viewModel(), onPrevious: () -> Unit
) {}
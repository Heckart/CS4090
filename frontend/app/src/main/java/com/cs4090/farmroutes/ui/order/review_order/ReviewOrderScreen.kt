package com.cs4090.farmroutes.ui.order.review_order

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.ui.order.review_order.ReviewOrderViewModel

@Composable
fun ReviewOrderScreen(
    viewModel: ReviewOrderViewModel = viewModel(), onNext: () -> Unit, onPrevious: () -> Unit
) {}
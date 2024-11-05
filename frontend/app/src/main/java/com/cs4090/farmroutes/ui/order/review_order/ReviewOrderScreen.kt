package com.cs4090.farmroutes.ui.order.review_order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme
import com.cs4090.farmroutes.utils.formatPrice


@Composable
fun ReviewOrderScreen(
    viewModel: ReviewOrderViewModel = viewModel(), onNext: () -> Unit, onPrevious: () -> Unit
) {
    val orderState = OrderRepository.order.observeAsState()
    val order = orderState.value

    var runningTotal: Double = 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Review Order",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Address Information
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Sending Order To:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(text = "${order?.deliveryAddress?.addressPrimary} ${order?.deliveryAddress?.addressSecondary}")
                Text(text = "${order?.deliveryAddress?.city}, ${order?.deliveryAddress?.state?.abbreviation} ${order?.deliveryAddress?.zipCode}")
            }
        }

        // Business Information
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Delivery Through:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(text = "${order?.businessInfo?.businessName}")
                Text(text = "${order?.businessInfo?.businessAddress?.addressPrimary} ${order?.businessInfo?.businessAddress?.addressSecondary}")
                Text(text = "${order?.businessInfo?.businessAddress?.city}, ${order?.businessInfo?.businessAddress?.state?.abbreviation} ${order?.businessInfo?.businessAddress?.zipCode}")
            }
        }

        // Shopping Cart Information
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Shopping Cart:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                if (order?.items != null) {
                    for (item in order.items.values.toList()) {
                        runningTotal += item.price * item.quantity
                    }
                    LazyColumn {
                        items(order.items.values.toList()) { item ->
                            runningTotal += item.price * item.quantity
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = "Name: ${item.name} (x${item.quantity}, ${
                                        formatPrice(
                                            item.price
                                        )
                                    } ea.)"
                                )
                                Text(
                                    text = "Cost: ${formatPrice(item.price * item.quantity)}",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                    }
                } else {
                    Text(text = "No items in cart.", fontStyle = FontStyle.Italic)
                }
            }
        }

        // Total Cost
        Text(
            text = "Total: ${formatPrice(runningTotal)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Navigation buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = onPrevious, modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Go Back")
            }
            Button(
                onClick = onNext, modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Confirm Order")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewOrderScreenPreview() {
    FarmroutesTheme {
        ReviewOrderScreen(onNext = {}, onPrevious = {})
    }
}
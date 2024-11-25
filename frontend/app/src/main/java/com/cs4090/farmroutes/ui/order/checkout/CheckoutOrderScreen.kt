package com.cs4090.farmroutes.ui.order.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme
import com.cs4090.farmroutes.utils.formatPrice

@Composable
fun CheckoutOrderScreen(
    viewModel: CheckoutOrderViewModel = viewModel(),
    checkOut: () -> Unit,
    onPrevious: () -> Unit
) {
    val orderState = OrderRepository.order.observeAsState()
    val order = orderState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Checkout",
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
                Text(
                    text = "Sending Order To:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
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
                Text(
                    text = "Delivery Through:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(text = "${order?.businessInfo?.businessName}")
                Text(text = "${order?.businessInfo?.businessAddress?.addressPrimary} ${order?.businessInfo?.businessAddress?.addressSecondary}")
                Text(text = "${order?.businessInfo?.businessAddress?.city}, ${order?.businessInfo?.businessAddress?.state?.abbreviation} ${order?.businessInfo?.businessAddress?.zipCode}")
            }
        }

        // Delivery Information
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Delivery Details",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(text = "Shopper:    ${order?.timeSlot?.firstName} ${order?.timeSlot?.lastName}")
                Text(text = "Order Time: ${order?.timeSlot?.fulfillmentTime.toString()}")
            }
        }

        // Order Total
        var orderTotal: Double = 0.0
        if (order?.items != null) {
            for (item in order.items.values.toList()) {
                orderTotal += item.price * item.quantity
            }
        }
        Text(
            text = "Total Cost: ${formatPrice(orderTotal)}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
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
                onClick = checkOut, modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Check Out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    FarmroutesTheme {
        CheckoutOrderScreen(checkOut = {}, onPrevious = {})
    }
}
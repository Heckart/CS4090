package com.cs4090.farmroutes.ui.order.schedule_delivery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.models.OrderTimeSlot
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme

@Composable
fun ScheduleDeliveryScreen(
    viewModel: ScheduleDeliveryViewModel = viewModel(), onNext: () -> Unit, onPrevious: () -> Unit
) {
    val availableTimeSlots = viewModel.availableTimeSlots.observeAsState(emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Schedule Your Delivery:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            items(availableTimeSlots.value) { timeSlot ->
                TimeSlotCard(timeSlot = timeSlot, onClick = { selectedTimeSlot ->
                    viewModel.updateSelectedTimeSlot(selectedTimeSlot)
                    onNext()
                })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(onClick = onPrevious) {
                Text("Go Back")
            }
        }
    }
}

@Composable
fun TimeSlotCard(timeSlot: OrderTimeSlot, onClick: (OrderTimeSlot) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${timeSlot.shopper.firstName} ${timeSlot.shopper.lastName}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = timeSlot.orderTime.toString(), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onClick(timeSlot) }, modifier = Modifier.align(Alignment.End)) {
                Text(text = "Select")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleDeliveryScreenPreview() {
    FarmroutesTheme {
        ScheduleDeliveryScreen(onNext = {}, onPrevious = {})
    }
}

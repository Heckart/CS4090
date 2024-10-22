package com.cs4090.farmroutes.ui.order.add_items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.models.OrderItem
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme


@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModel = viewModel(), onNext: () -> Unit, onPrevious: () -> Unit
) {
    val availableItems by viewModel.availableItems.observeAsState(emptyList())

    Column() {
        Text(text = "Choose Items to Order:")
        LazyColumn() {
            items(availableItems) { item ->
                ItemCard(item = item, onClick = {})
            }
        }
        Row() {
            Button(
                onClick = onPrevious
            ) {
                Text("Go Back")
            }
            Button(
                onClick = onNext
            ) {
                Text("Next")
            }
        }

    }

}

@Composable
fun ItemCard(item: OrderItem, onClick: () -> Unit) {
    Card() {
        Text(text = item.name)
        Text(text = "$${item.price}")
        Text(text = "Quantity: ${item.quantity}")
        Row() {
            Button(onClick = {
                // TODO - Decrement Item Count
            }) {
                Text("-")
            }
            Button(onClick = {
                // TODO - Increment Item Count
            }) {
                Text("+")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCard() {
    FarmroutesTheme {
        AddItemsScreen(onNext = {}, onPrevious = {})
    }
}
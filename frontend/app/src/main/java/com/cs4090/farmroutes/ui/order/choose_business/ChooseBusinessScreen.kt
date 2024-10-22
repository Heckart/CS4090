package com.cs4090.farmroutes.ui.order.choose_business

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme

@Composable
fun ChooseBusinessScreen(
    viewModel: ChooseBusinessViewModel = viewModel(), onNext: () -> Unit, onPrevious: () -> Unit
) {
    val availableBusinesses by viewModel.availableBusinesses.observeAsState(emptyList())

    Column() {
        Text(text = "Choose a Location to Order From:")
        LazyColumn() {
            items(availableBusinesses) { business ->
                BusinessItemCard(business = business, onClick = onNext)
            }
        }
        Row() {
            Button(
                onClick = onPrevious
            ) {
                Text("Previous")
            }

        }
    }
}

@Composable
fun BusinessItemCard(business: BusinessInformation, onClick: () -> Unit) {
    Card() {
        Text(text = business.businessName)
        Text(text = business.businessAddress.addressPrimary)
        business.businessAddress.addressSecondary?.let {
            Text(text = it)
        }
        Text(text = "${business.businessAddress.city}, ${business.businessAddress.state} ${business.businessAddress.zipCode}")
        Button(onClick = onClick) {
            Text(text = "Select")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseBusinessScreenPreview() {
    FarmroutesTheme {
        ChooseBusinessScreen(onNext = {}, onPrevious = {})
    }
}
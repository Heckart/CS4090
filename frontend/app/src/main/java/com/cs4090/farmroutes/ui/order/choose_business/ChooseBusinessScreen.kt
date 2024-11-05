package com.cs4090.farmroutes.ui.order.choose_business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme

@Composable
fun ChooseBusinessScreen(
    viewModel: ChooseBusinessViewModel = viewModel(),
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    val availableBusinesses by viewModel.availableBusinesses.observeAsState(emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Choose a Location to Order From:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            items(availableBusinesses) { business ->
                BusinessItemCard(business = business, onClick = { selectedBusiness ->
                    viewModel.updateSelectedBusiness(selectedBusiness)
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
fun BusinessItemCard(business: BusinessInformation, onClick: (BusinessInformation) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = business.businessName,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = business.businessAddress.addressPrimary, fontSize = 16.sp)
            business.businessAddress.addressSecondary?.let {
                Text(text = it, fontSize = 14.sp)
            }
            Text(
                text = "${business.businessAddress.city}, ${business.businessAddress.state.abbreviation} ${business.businessAddress.zipCode}",
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onClick(business) }, modifier = Modifier.align(Alignment.End)) {
                Text(text = "Select")
            }
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

package com.cs4090.farmroutes.ui.order.select_address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme

@Composable
fun SelectAddressScreen(
    viewModel: SelectAddressViewModel = viewModel(), onNext: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Enter Delivery Address Information",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = viewModel.primaryAddress,
            onValueChange = { primaryAddress -> viewModel.updatePrimaryAddressInfo(primaryAddress) },
            label = { Text("Primary Address Information") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = viewModel.secondaryAddress,
            onValueChange = { secondaryAddress ->
                viewModel.updateSecondaryAddressInfo(
                    secondaryAddress
                )
            },
            label = { Text("Secondary Address Information") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = viewModel.city,
            onValueChange = { city -> viewModel.updateCityInfo(city) },
            label = { Text("City") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        StateDropdown(selectedState = viewModel.state,
            onStateSelected = { viewModel.updateStateInfo(it) })

        OutlinedTextField(
            value = viewModel.zipCode,
            onValueChange = { zipCode -> viewModel.updateZipCodeInfo(zipCode) },
            label = { Text("Zip Code") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                viewModel.updateOrderAddress()
                onNext()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Next")
        }
    }
}

@Composable
fun StateDropdown(
    selectedState: USState, onStateSelected: (USState) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = selectedState.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { dropdownExpanded = true },
            fontSize = 16.sp
        )

        // Dropdown menu
        DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
            USState.entries.forEach { state ->
                DropdownMenuItem(text = { Text(text = state.toString()) }, onClick = {
                    onStateSelected(state)
                    dropdownExpanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectAddressScreenPreview() {
    FarmroutesTheme {
        SelectAddressScreen(onNext = {})
    }
}

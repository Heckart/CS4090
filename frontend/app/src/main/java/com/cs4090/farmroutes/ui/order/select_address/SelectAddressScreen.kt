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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme


@Composable
fun SelectAddressScreen(
    viewModel: SelectAddressViewModel = viewModel(), onNext: () -> Unit
) {
    var selectedState by remember { mutableStateOf(USState.ALABAMA) }

    Column() {
        Text(
            text = "Enter Delivery Address Information",

            )
        OutlinedTextField(
            value = viewModel.primaryAddress,
            onValueChange = { primaryAddress -> viewModel.updatePrimaryAddressInfo(primaryAddress) },
            label = { Text("Primary Address Information") }
        )
        OutlinedTextField(
            value = viewModel.secondaryAddress,
            onValueChange = { secondaryAddress ->
                viewModel.updateSecondaryAddressInfo(
                    secondaryAddress
                )
            },
            label = { Text("Secondary Address Information") }
        )
        OutlinedTextField(
            value = viewModel.city,
            onValueChange = { city -> viewModel.updateCityInfo(city) },
            label = { Text("City") }
        )
        StateDropdown(
            selectedState = selectedState,
            onStateSelected = { selectedState = it }
        )
        OutlinedTextField(
            value = viewModel.zipCode,
            onValueChange = { zipCode -> viewModel.updateZipCodeInfo(zipCode) },
            label = { Text("Zip Code") }
        )
        Button(
            onClick = {
                viewModel.updateOrderAddress()
                onNext()
            }
        ) {
            Text("Next")
        }
    }
}

@Composable
fun StateDropdown(
    selectedState: USState,
    onStateSelected: (USState) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = selectedState.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { dropdownExpanded = true }
        )

        // Dropdown menu
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            USState.entries.forEach { state ->
                DropdownMenuItem(
                    text = { Text(text = state.toString()) },
                    onClick = {
                        onStateSelected(state)
                        dropdownExpanded = false
                    }
                )
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
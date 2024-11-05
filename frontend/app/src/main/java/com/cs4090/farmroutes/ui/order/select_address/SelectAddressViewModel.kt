package com.cs4090.farmroutes.ui.order.select_address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.data.repository.OrderRepository


class SelectAddressViewModel() : ViewModel() {
    val order = OrderRepository.order

    var primaryAddress by mutableStateOf("")
    var secondaryAddress by mutableStateOf("")
    var city by mutableStateOf("")
    var state by mutableStateOf(USState.ALABAMA)
    var zipCode by mutableStateOf("")

    fun updatePrimaryAddressInfo(input: String) {
        primaryAddress = input
    }

    fun updateSecondaryAddressInfo(input: String) {
        secondaryAddress = input
    }

    fun updateCityInfo(input: String) {
        city = input
    }

    fun updateStateInfo(input: USState) {
        state = input
    }

    fun updateZipCodeInfo(input: String) {
        zipCode = input
    }

    fun updateOrderAddress() {
        val address = AddressInformation(
            addressPrimary = primaryAddress,
            addressSecondary = secondaryAddress,
            city = city,
            state = state,
            zipCode = zipCode
        )
        OrderRepository.updateAddressInformation(address)
    }
}
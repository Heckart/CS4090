package com.cs4090.farmroutes.ui.order.choose_business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4090.farmroutes.data.models.AddressInformation
import com.cs4090.farmroutes.data.models.BusinessInformation
import com.cs4090.farmroutes.data.models.USState
import com.cs4090.farmroutes.data.repository.OrderRepository

class ChooseBusinessViewModel : ViewModel() {
    val order = OrderRepository.order

    // TODO - Eventually load this in from the DB
    private val _exampleStores = MutableLiveData<List<BusinessInformation>>(
        listOf(
            BusinessInformation(
                businessName = "Fresh Mart",
                businessAddress = AddressInformation(
                    addressPrimary = "123 Oak St",
                    addressSecondary = "Suite A",
                    city = "St. Louis",
                    state = USState.MISSOURI,
                    zipCode = "63101"
                )
            ),
            BusinessInformation(
                businessName = "Green Grocer",
                businessAddress = AddressInformation(
                    addressPrimary = "456 Elm St",
                    city = "Springfield",
                    state = USState.ILLINOIS,
                    zipCode = "62701"
                )
            ),
            BusinessInformation(
                businessName = "Downtown Market",
                businessAddress = AddressInformation(
                    addressPrimary = "789 Pine St",
                    addressSecondary = "Building B",
                    city = "Kansas City",
                    state = USState.KANSAS,
                    zipCode = "66101"
                )
            )
        )
    )

    // TODO - Eventually get _exampleStores from DB
    val availableBusinesses: LiveData<List<BusinessInformation>> = _exampleStores

    fun updateSelectedBusiness(selectedBusiness: BusinessInformation) {
        OrderRepository.updateBusinessInformation(selectedBusiness)
    }

}
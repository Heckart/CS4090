package com.cs4090.farmroutes.data.models

/**
 * A collection of the information of where a delivery is to.
 *
 * @property addressPrimary The primary address line (street name and number).
 * @property addressSecondary The secondary address line (secondary descriptor and number).
 * @property city The city where the delivery is to.
 * @property state The state/province where the delivery is to.
 * @property zipCode The postal code of where the delivery is to.
 */
data class AddressInformation(
    var addressPrimary: String,
    var addressSecondary: String? = null,
    var city: String,
    var state: USState,
    var zipCode: String,
)
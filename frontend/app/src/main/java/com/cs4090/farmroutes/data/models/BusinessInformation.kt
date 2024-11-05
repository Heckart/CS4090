package com.cs4090.farmroutes.data.models

/**
 * A collection of information about a business.
 *
 * @property businessName The name of the business.
 * @property businessAddress The address information of the business.
 */
data class BusinessInformation(
    var businessName: String,
    var businessAddress: AddressInformation
)

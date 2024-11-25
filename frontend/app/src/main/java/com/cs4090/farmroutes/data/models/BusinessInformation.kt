package com.cs4090.farmroutes.data.models

import com.google.gson.annotations.SerializedName

/**
 * A collection of information about a business.
 *
 * @property businessID The unique ID of the business.
 * @property businessName The name of the business.
 * @property businessAddress The address information of the business.
 */
data class BusinessInformation(
    @SerializedName("businessID")
    val businessID: String,
    @SerializedName("businessName")
    val businessName: String,
    @SerializedName("addressInfo")
    val businessAddress: AddressInformation
)

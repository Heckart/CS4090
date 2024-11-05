package com.cs4090.farmroutes.data.models

/**
 * A collection of information for a user.
 *
 * TODO: add more attributes as needed
 *
 * @property userID The string used to uniquely identify the user.
 * @property firstName The first name of the user.
 * @property lastName The last name of the user
 * @property primaryAddress The primary address information of the user. Default delivery location.
 */
data class User(
    var userID: String,
    var firstName: String,
    var lastName: String,
    var primaryAddress: String,
)

/**
 * A collection of information for a shopper.
 *
 * TODO: add more attributes as needed
 *
 * @property shopperID The string used to uniquely identify the shopper.
 * @property firstName The first name of the shopper.
 * @property lastName The last name of the shopper
 */
data class Shopper(
    var shopperID: String,
    var firstName: String,
    var lastName: String,
)

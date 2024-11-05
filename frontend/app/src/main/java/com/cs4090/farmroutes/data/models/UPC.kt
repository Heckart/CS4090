package com.cs4090.farmroutes.data.models

/**
 * Object for determining if an Integer is a valid UPC.
 */
data class UPC(val upc: Int) {
    init {
        // TODO - Further requirements for UPC?
        require(upc > 0) { "UPC value cannot be a negative integer." }
    }
}

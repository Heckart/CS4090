package com.cs4090.farmroutes.data.models

data class UPC(val upc: Int) {
    init{
        // TODO - Further requirements for UPC?
        require(upc > 0) {"UPC value cannot be a negative integer."}
    }
}

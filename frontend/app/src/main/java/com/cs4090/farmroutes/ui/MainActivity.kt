package com.cs4090.farmroutes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.cs4090.farmroutes.ui.order.OrderNavigation
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FarmroutesTheme {
                Surface() {
                    OrderNavigation()
                }
            }
        }
    }
}
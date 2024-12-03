package com.cs4090.farmroutes.ui

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.cs4090.farmroutes.ui.order.OrderNavigation
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        )
        setContent {
            FarmroutesTheme {
                Surface {
                    OrderNavigation()
                }
            }
        }
    }
}
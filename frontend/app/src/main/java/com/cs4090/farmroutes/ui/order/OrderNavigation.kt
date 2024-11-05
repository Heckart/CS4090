package com.cs4090.farmroutes.ui.order

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs4090.farmroutes.ui.order.add_items.AddItemsScreen
import com.cs4090.farmroutes.ui.order.checkout.CheckoutOrderScreen
import com.cs4090.farmroutes.ui.order.choose_business.ChooseBusinessScreen
import com.cs4090.farmroutes.ui.order.review_order.ReviewOrderScreen
import com.cs4090.farmroutes.ui.order.schedule_delivery.ScheduleDeliveryScreen
import com.cs4090.farmroutes.ui.order.select_address.SelectAddressScreen


sealed class OrderScreen(val route: String) {
    object SelectAddress : OrderScreen("select_address")
    object ChooseBusiness : OrderScreen("choose_business")
    object AddItems : OrderScreen("add_items")
    object ReviewOrder : OrderScreen("review_order")
    object ScheduleDelivery : OrderScreen("schedule_delivery")
    object CheckoutOrder : OrderScreen("checkout_order")
}

@Composable
fun OrderNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = OrderScreen.SelectAddress.route
    ) {
        composable(OrderScreen.SelectAddress.route) {
            SelectAddressScreen(
                onNext = { navController.navigate(OrderScreen.ChooseBusiness.route) }
            )
        }
        composable(OrderScreen.ChooseBusiness.route) {
            ChooseBusinessScreen(
                onNext = { navController.navigate(OrderScreen.AddItems.route) },
                onPrevious = { navController.navigate(OrderScreen.SelectAddress.route) }
            )
        }
        composable(OrderScreen.AddItems.route) {
            AddItemsScreen(
                onNext = { navController.navigate(OrderScreen.ReviewOrder.route) },
                onPrevious = { navController.navigate(OrderScreen.ChooseBusiness.route) }
            )
        }
        composable(OrderScreen.ReviewOrder.route) {
            ReviewOrderScreen(
                onNext = { navController.navigate(OrderScreen.ScheduleDelivery.route) },
                onPrevious = { navController.navigate(OrderScreen.AddItems.route) }
            )
        }
        composable(OrderScreen.ScheduleDelivery.route) {
            ScheduleDeliveryScreen(
                onNext = { navController.navigate(OrderScreen.CheckoutOrder.route) },
                onPrevious = { navController.navigate(OrderScreen.ReviewOrder.route) }
            )
        }
        composable(OrderScreen.CheckoutOrder.route) {
            CheckoutOrderScreen(
                checkOut = {/* TODO - Checkout Logic */ },
                onPrevious = { navController.navigate(OrderScreen.ScheduleDelivery.route) }
            )
        }
    }
}
package com.cs4090.farmroutes.ui.order.add_items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4090.farmroutes.data.repository.OrderRepository
import com.cs4090.farmroutes.ui.theme.FarmroutesTheme
import com.cs4090.farmroutes.utils.formatPrice


@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModel = viewModel(),
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    // All available items for purchase
    val availableItems = viewModel.availableItems

    // The current order
    val order by OrderRepository.order.observeAsState()
    val selectedBusiness = order?.businessInfo

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Choose Items to Order:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Ordering from: ${selectedBusiness?.businessName ?: "No Business Selected"}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "${order?.businessInfo?.businessAddress?.addressPrimary} ${order?.businessInfo?.businessAddress?.addressSecondary}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = "${order?.businessInfo?.businessAddress?.city}, ${order?.businessInfo?.businessAddress?.state?.abbreviation} ${order?.businessInfo?.businessAddress?.zipCode}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn {
                items(availableItems!!.entries.toList()) { (upc, item) ->
                    order?.items?.get(upc)?.quantity ?: 0
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatPrice(item.price),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "In Cart: ${item.quantity}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.7f
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    8.dp, Alignment.CenterHorizontally
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.removeItemFromCart(
                                            item.upc
                                        )
                                    },
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp, vertical = 4.dp
                                    )
                                ) {
                                    Text("-")
                                }
                                Text(
                                    text = "Quantity: ${item.quantity}",
                                    fontSize = 16.sp
                                )
                                Button(
                                    onClick = { viewModel.addItemToCart(item.upc) },
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp, vertical = 4.dp
                                    )
                                ) {
                                    Text("+")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = onPrevious, modifier = Modifier.weight(1f)
            ) {
                Text("Go Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onNext, modifier = Modifier.weight(1f)
            ) {
                Text("Review Order")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemsScreenPreview() {
    FarmroutesTheme {
        AddItemsScreen(onNext = {}, onPrevious = {})
    }
}
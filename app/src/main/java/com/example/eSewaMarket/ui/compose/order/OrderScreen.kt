package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.ui.compose.AppToolBar
import com.example.eSewaMarket.ui.compose.component.CommonError
import com.example.eSewaMarket.ui.compose.component.CommonLoading

@Composable
fun OrderScreen(
    uiState: OrderUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val tabs = listOf(
        "All",
        "Pending",
        "Complete"
    )

    Scaffold(
        containerColor = colorResource(R.color.background),

        topBar = {
            AppToolBar(
                modifier = Modifier
                    .statusBarsPadding(),
                onBackClick = onBackClick,
                title = {
                    Text(
                        "My Order",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                },
                bottomContent = {
                    TabCard(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { index ->
                            selectedTabIndex = index
                        }
                    )
                }
            )
        }

    ) { innerPadding ->

        when (uiState) {

            OrderUiState.Loading -> {
                CommonLoading()
            }

            is OrderUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Text(
                        text = tabs[selectedTabIndex],
                        fontSize = 20.sp
                    )
                }
            }

            is OrderUiState.Error -> {
                CommonError(
                    message = uiState.message,
                    onRetry = onRetry
                )
            }
        }
    }
}
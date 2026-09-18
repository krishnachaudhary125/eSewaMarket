package com.example.eSewaMarket.ui.compose.order

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    onApplyClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                "Filter by",
                fontSize = 20.sp,
                letterSpacing = 1.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_dark_400),
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        "From"
                    )

                    DateInput()
                }

                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        "To"
                    )

                    DateInput()
                }
            }

            Button(
                onClick = onApplyClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    "APPLY",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}
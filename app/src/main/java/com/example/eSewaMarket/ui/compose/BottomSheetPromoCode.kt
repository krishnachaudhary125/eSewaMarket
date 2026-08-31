package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetPromoCode(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    onApplyClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                "Promocode",
                fontSize = 20.sp,
                letterSpacing = 1.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_dark_400),
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )

            Text(
                "Enter promocode",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp,
                letterSpacing = 1.sp,
                color = colorResource(id = R.color.text_dark_300),
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 4.dp
                    )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.compose_text_field),
                        shape = RoundedCornerShape(16.dp)
                    ),
                state = rememberTextFieldState(),
                placeholder = {
                    Text(
                        text = "Promocode",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 2.sp,
                        color = colorResource(R.color.text_dark_100)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = onCancelClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.text_dark_300),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            end = 8.dp
                        )
                ) {
                    Text(
                        "CANCEL",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp,
                        letterSpacing = 4.sp,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                    )
                }

                Button(
                    onClick = onApplyClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 8.dp
                        )
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
}
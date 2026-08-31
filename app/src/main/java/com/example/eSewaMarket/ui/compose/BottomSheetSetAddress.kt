package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSetAddress(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    onSetAddressClick: () -> Unit,
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
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_set_address),
                contentDescription = "Set address image",
                modifier = Modifier
                    .padding(top = 48.dp)
            )

            Text(
                "No address added yet !",
                color = colorResource(R.color.text_dark_400),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            Text(
                "You have not added any shipping address.",
                color = colorResource(R.color.text_dark_300),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            Button(
                onClick = onSetAddressClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    "SET ADDRESS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = onCancelClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = colorResource(R.color.text_dark_300)
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                Text(
                    "CANCEL",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}
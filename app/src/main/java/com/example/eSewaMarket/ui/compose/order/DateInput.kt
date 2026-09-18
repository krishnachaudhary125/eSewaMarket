package com.example.eSewaMarket.ui.compose.order

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eSewaMarket.R
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateInput() {

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var selectedDate by remember {
        mutableStateOf("")
    }

    Column{
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Select Date")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calender),
                        contentDescription = "Select date",
                        tint = Color.Unspecified
                    )
                }
            },
            modifier = Modifier
                .background(
                    color = colorResource(id = R.color.compose_text_field),
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        if (showDatePicker) {

            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            val selectedMillis =
                                datePickerState.selectedDateMillis

                            if (selectedMillis != null) {

                                val date = Instant
                                    .ofEpochMilli(selectedMillis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()

                                selectedDate = date.toString()
                            }

                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {

                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}
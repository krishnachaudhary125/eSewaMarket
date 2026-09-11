package com.example.eSewaMarket.ui.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eSewaMarket.BuildConfig
import com.f1soft.esewapaymentsdk.EsewaConfiguration
import com.f1soft.esewapaymentsdk.EsewaPayment
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity

@Suppress("DEPRECATION")
class EsewaPayment : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PAYMENT = 1

        private const val CLIENT_ID = BuildConfig.ESEWA_CLIENT_ID
        private const val CLIENT_SECRET_KEY = BuildConfig.ESEWA_SECRET_KEY
    }

    private lateinit var eSewaConfiguration: EsewaConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eSewaConfiguration = EsewaConfiguration(
            clientId = CLIENT_ID,
            secretKey = CLIENT_SECRET_KEY,
            environment = EsewaConfiguration.ENVIRONMENT_TEST
        )

        makePayment()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != REQUEST_CODE_PAYMENT) {
            return
        }

        when (resultCode) {
            RESULT_OK -> {

                val message = data?.getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE)
                Log.d("eSewa_Payment", "Success: $message")

                setResult(RESULT_OK, Intent().apply {
                    putExtra("orderNumber", intent.getStringExtra("orderNumber"))
                })
                finish()
            }

            RESULT_CANCELED -> {
                setResult(RESULT_CANCELED)
                finish()
            }

            EsewaPayment.RESULT_EXTRAS_INVALID -> {

                val message = data?.getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE)
                Log.e("eSewa_Payment", "Payment Error: $message")

                setResult(RESULT_CANCELED, Intent().apply {
                    putExtra("errorMessage", message)
                })
                finish()
            }
        }
    }

    private fun makePayment() {

        val amount = intent.getDoubleExtra("grandTotal", 0.0)

        val orderId = intent.getStringExtra("orderNumber")

        if (orderId.isNullOrBlank()) {
            Toast.makeText(this, "Order information is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val eSewaPayment = EsewaPayment(
            amount.toString(),
            "eSewaMarket Order",
            orderId,
            "https://www.google.com"
        )

        val paymentIntent = Intent(this, EsewaPaymentActivity::class.java).apply {
            putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration)
            putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment)
        }
        startActivityForResult(paymentIntent, REQUEST_CODE_PAYMENT)
    }
}
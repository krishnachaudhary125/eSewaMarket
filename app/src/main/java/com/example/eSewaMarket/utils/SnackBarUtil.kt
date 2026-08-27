package com.example.eSewaMarket.utils

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.eSewaMarket.R
import com.google.android.material.snackbar.Snackbar

object SnackBarUtil {

    fun show(
        view: View,
        context: Context,
        text: String,
        anchorView: View? = null,
        actionText: String? = null,
        duration: Int = Snackbar.LENGTH_LONG,
        action: (() -> Unit)? = null
    ) {
        val snackBar = Snackbar.make(view, text, duration)

        anchorView?.let {
            snackBar.setAnchorView(it)
        }

        snackBar.setBackgroundTint(
            ContextCompat.getColor(context, R.color.black)
        )

        snackBar.setTextColor(
            ContextCompat.getColor(context, R.color.white)
        )

        snackBar.setActionTextColor(
            ContextCompat.getColor(context, R.color.green)
        )

        snackBar.view.setPadding(
            snackBar.view.paddingStart,
            8,
            snackBar.view.paddingEnd,
            8
        )

        if (actionText != null && action != null) {
            snackBar.setAction(actionText) {
                action()
            }
        }

        snackBar.show()
    }
}
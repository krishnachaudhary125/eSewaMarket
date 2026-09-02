package com.example.eSewaMarket.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.eSewaMarket.R

class SpinnerAdapter(
    context: Context,
    private val items: List<String>,
    private val hint: String
) : ArrayAdapter<String>(
    context,
    R.layout.spinner_items,
    items
) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = super.getView(
            position,
            convertView,
            parent
        ) as TextView

        view.text = if (position == 0) {
            hint
        } else {
            items[position]
        }

        return view
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        if (position == 0) {
            val view = View(context)
            view.layoutParams = AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
            )
            return view
        }

        val view = LayoutInflater.from(context).inflate(
            R.layout.spinner_dropdown_items,
            parent,
            false
        ) as TextView

        view.text = items[position]

        return view
    }
}
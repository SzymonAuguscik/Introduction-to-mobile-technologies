package com.example.szymon_auguscik_czw_8_00

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyAdapter(var dataSet: Array<CurrencyDetails>, var context: Context) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val codeTextView: TextView
        val rateTextView: TextView
        val flagImageView: ImageView
        val arrowImageView: ImageView

        init {
            codeTextView = view.findViewById(R.id.currencyCodeTextView)
            rateTextView = view.findViewById(R.id.currencyRateTextView)
            flagImageView = view.findViewById(R.id.currencyImageView)
            arrowImageView = view.findViewById(R.id.arrowImageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.currency_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currency = dataSet[position]
        val progress = currency.todayRate > currency.yesterdayRate
        viewHolder.codeTextView.text = currency.currencyCode
        viewHolder.rateTextView.text = currency.todayRate.toString()
        viewHolder.flagImageView.setImageResource(currency.flag)
        if (progress) {
            viewHolder.arrowImageView.setImageResource(R.drawable.wolf_up)
        }
        else {
            viewHolder.arrowImageView.setImageResource(R.drawable.wolf_down)
        }
        viewHolder.itemView.setOnClickListener{goToDetails(currency.currencyCode)}
    }

    private fun goToDetails(currencyCode: String) {
        val intent = Intent(context, CurrencyChartsActivity::class.java).apply {
            putExtra("currencyCode", currencyCode)
        }
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

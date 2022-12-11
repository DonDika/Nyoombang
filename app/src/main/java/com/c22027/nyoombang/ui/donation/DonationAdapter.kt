package com.c22027.nyoombang.ui.donation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.databinding.DonationItemLayoutBinding
import com.c22027.nyoombang.utils.Utilization
import java.util.TimeZone

class DonationAdapter(
var transactions: ArrayList<UserTransaction>,
) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    //add data
    fun setData(dataTransaction: List<UserTransaction>) {
        transactions.clear()
        transactions.addAll(dataTransaction)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DonationItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]

        //Log.e("adapter", campaign.toString() )

        holder.binding.apply {

            tvEventName.text = transaction.eventName
            val formatAmount = Utilization.amountDonationFormat(transaction.amount.toInt())
            tvAmount.text = "Rp. $formatAmount"
            tvStatus.text = transaction.status
            tvDate.text = Utilization.formatDate(transaction.transactionDate, TimeZone.getDefault().id)
            tvTime.text = transaction.transactionTime

        }

    }

    override fun getItemCount(): Int {
        return transactions.size
    }


    inner class ViewHolder(val binding: DonationItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    interface AdapterListener {
        fun onClick(campaign: EventDataClass)
    }
}
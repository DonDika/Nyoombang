package com.c22027.nyoombang.ui.profile.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.databinding.ItemListHistoryBinding
import com.c22027.nyoombang.utils.Utilization

class UserHistoryAdapter(
    var userTransaction: ArrayList<UserTransaction>
): RecyclerView.Adapter<UserHistoryAdapter.ViewHolder>() {


    fun setData(dataUserTransaction: List<UserTransaction>){
        userTransaction.clear()
        userTransaction.addAll(dataUserTransaction)
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = userTransaction[position]
        holder.binding.apply {
            tvCampaignName.text = userData.eventName
            val formatAmount = Utilization.amountDonationFormat(userData.amount.toInt())
            tvAmount.text = "Rp. $formatAmount"
            tvStatus.text = "Done"
            tvTime.text = userData.transactionTime
        }

    }

    override fun getItemCount(): Int {
        return userTransaction.size
    }


}
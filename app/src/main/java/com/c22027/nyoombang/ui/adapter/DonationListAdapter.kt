package com.c22027.nyoombang.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.c22027.nyoombang.data.model.UserTransaction
import com.c22027.nyoombang.databinding.DonationItemLayoutBinding
import com.c22027.nyoombang.utils.Utilization
import java.util.*

/*
class DonationListAdapter : ListAdapter<Donation, DonationListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DonationItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: DonationItemLayoutBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(donation: Donation) {
            binding.apply {
                tvCommunityName.text = donation.to
                tvAmount.text = "Rp ${donation.amount}"
                tvLocation.text = donation.city
                tvDate.text = Utilization.formatDate(donation.date, TimeZone.getDefault().id)
                tvTime.text = Utilization.formatTime(donation.date, TimeZone.getDefault().id)
                if (donation.isApproved) {
                    tvStatus.text = "Approved"
                } else {
                    tvStatus.text = "Denied"
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Donation> =
            object : DiffUtil.ItemCallback<Donation>() {
                override fun areItemsTheSame(oldUser: Donation, newUser: Donation): Boolean {
                    return oldUser.date == newUser.date
                }

                override fun areContentsTheSame(oldUser: Donation, newUser: Donation): Boolean {
                    return oldUser == newUser
                }
            }
    }
}

*/
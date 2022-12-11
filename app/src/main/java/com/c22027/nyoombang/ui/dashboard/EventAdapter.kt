package com.c22027.nyoombang.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ItemsCampaignBinding
import com.c22027.nyoombang.utils.Utilization
import java.util.TimeZone

class EventAdapter (
    private val context: Context,
    var events: ArrayList<EventDataClass>,
    var listener: AdapterListener?
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    //add data
    fun setData(dataCampaign: List<EventDataClass>){
        events.clear()
        events.addAll(dataCampaign)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]

        //Log.e("adapter", campaign.toString() )

        holder.binding.apply {
            Glide.with(context).load(event.eventPicture).into(circleImageView)
            tvCampaignName.text = event.eventName
            val formatAmount = Utilization.amountDonationFormat(event.totalAmount!!)
            tvAmountCampaign.text = "Rp. $formatAmount"
            tvUsername.text = event.userName
            tvCampaignDate.text = Utilization.formatDate(event.endOfDate, TimeZone.getDefault().id)
            Log.d("eventError",event.eventPicture.toString())

            container.setOnClickListener {
                listener?.onClick(event)
            }
        }

    }

    override fun getItemCount(): Int {
        return events.size
    }


    inner class ViewHolder(val binding: ItemsCampaignBinding): RecyclerView.ViewHolder(binding.root) {

    }


    interface AdapterListener {
        fun onClick(campaign: EventDataClass)
    }

}
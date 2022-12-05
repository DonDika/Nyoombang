package com.c22027.nyoombang.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ItemsCampaignBinding

class EventAdapter (
    private val context: Context,
    var campaigns: ArrayList<EventDataClass>,
    var listener: AdapterListener?
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    //add data
    fun setData(dataCampaign: List<EventDataClass>){
        campaigns.clear()
        campaigns.addAll(dataCampaign)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val campaign = campaigns[position]

        //Log.e("adapter", campaign.toString() )

        holder.binding.apply {
            Glide.with(context).load(campaign.eventPicture).into(circleImageView)
            tvCampaignName.text = campaign.eventName
            tvAmountCampaign.text = campaign.totalAmount.toString()
            Log.d("eventError",campaign.eventPicture.toString())

            container.setOnClickListener {
                listener?.onClick(campaign)
            }
        }

    }

    override fun getItemCount(): Int {
        return campaigns.size
    }


    inner class ViewHolder(val binding: ItemsCampaignBinding): RecyclerView.ViewHolder(binding.root) {

    }


    interface AdapterListener {
        fun onClick(campaign: EventDataClass)
    }

}
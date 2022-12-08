package com.c22027.nyoombang.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.EventResponse
import com.c22027.nyoombang.databinding.ActivityDashboardBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.details.DetailsEventActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var eventAdapter: EventAdapter
    private val viewmodel by viewModels<DashboardViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper= SharedPreferencesHelper(this)
        reference = FirebaseDatabase.getInstance().getReference("Event")
        setupListener()
        setupAdapter()

    }

    override fun onStart() {
        super.onStart()
        getEvent()
    }
    private fun setupListener() {


    }

    private fun setupAdapter() {
        eventAdapter = EventAdapter(this@DashboardActivity,arrayListOf(), object : EventAdapter.AdapterListener {
            override fun onClick(campaign: EventDataClass) {
                showSelectedEvent(campaign)

            }
        })
        binding.rvUsers.adapter =eventAdapter
    }

    private fun getEventForUser(response: EventResponse){
        val events : ArrayList<EventDataClass> = arrayListOf()
        response.items?.let { event ->
            event.forEach {
                val eventData = EventDataClass(
                    it.event_id,
                    it.user_id,
                    it.eventName,
                    it.eventPicture,
                    it.eventDescription,
                    it.endOfDate,
                    it.totalAmount
                )

                Log.d("picture",it.eventPicture.toString())
                events.add(eventData)

            }

        }
        eventAdapter.setData(events)

    }
    private fun showSelectedEvent(eventDataClass: EventDataClass){
        intent = Intent(this@DashboardActivity, DetailsEventActivity::class.java)
        intent.putExtra(DetailsEventActivity.EXTRA_EVENT,eventDataClass.event_id)
        startActivity(intent)
    }
    private fun getEvent(){
        viewmodel.getEventUser().observe(this){
            getEventForUser(it)
        }

    }


}
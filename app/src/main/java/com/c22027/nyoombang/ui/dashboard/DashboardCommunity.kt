package com.c22027.nyoombang.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityDashboardCommunityBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.ui.addevent.AddEventActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardCommunity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardCommunityBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore}
    private val sharedPreferences by lazy { SharedPreferencesHelper(this) }

    private lateinit var eventAdapter: EventAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
        setupAdapter()
    }

    override fun onStart() {
        super.onStart()

        getEvent()
    }


    private fun setupListener() {
        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }
    }


    private fun setupAdapter() {
        eventAdapter = EventAdapter(this@DashboardCommunity, arrayListOf(),
            object : EventAdapter.AdapterListener {
                override fun onClick(campaign: EventDataClass) {

                }
            })
        binding.rvUsers.adapter = eventAdapter
    }


    private fun getEvent() {
        val events: ArrayList<EventDataClass> = arrayListOf()
        fireStore.collection("Event")
            .whereEqualTo("user_id", sharedPreferences.prefUid.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    document.forEach {
                        val eventData = EventDataClass(
                            it.data["event_id"].toString(),
                            it.data["user_id"].toString(),
                            it.data["eventName"].toString(),
                            it.data["eventPicture"].toString(),
                            it.data["eventDescription"].toString(),
                            it.data["endOfDate"].toString(),
                            it.data["totalAmount"].toString()
                        )
                        events.add(eventData)
                    }
                }
                eventAdapter.setData(events)
            }
    }



}
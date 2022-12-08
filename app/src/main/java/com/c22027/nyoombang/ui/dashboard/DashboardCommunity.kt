package com.c22027.nyoombang.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.EventResponse
import com.c22027.nyoombang.databinding.ActivityDashboardCommunityBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.addevent.AddEventActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardCommunity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardCommunityBinding
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var eventAdapter: EventAdapter
    private val db by lazy { Firebase.firestore}
    private val viewmodel by viewModels<DashboardViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        reference = FirebaseDatabase.getInstance().getReference("Event")
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
        eventAdapter = EventAdapter(
            this@DashboardCommunity,
            arrayListOf(),
            object : EventAdapter.AdapterListener {
                override fun onClick(campaign: EventDataClass) {

                }
            })
        binding.rvUsers.adapter = eventAdapter
    }

    private fun getEvent() {
        val events: ArrayList<EventDataClass> = arrayListOf()
        db.collection("Event").whereEqualTo("user_id", sharedPreferencesHelper.prefUid.toString()).get().addOnCompleteListener { task ->
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
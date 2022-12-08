package com.c22027.nyoombang.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityDashboardBinding
import com.c22027.nyoombang.ui.details.DetailsEventActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore}
    //private val sharedPreferences by lazy { SharedPreferencesHelper(this) }
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


    }

    private fun setupAdapter() {
        eventAdapter = EventAdapter(this@DashboardActivity,arrayListOf(), object : EventAdapter.AdapterListener {
            override fun onClick(campaign: EventDataClass) {
                showSelectedEvent(campaign)

            }
        })
        binding.rvUsers.adapter =eventAdapter
    }

    private fun showSelectedEvent(eventDataClass: EventDataClass){
        intent = Intent(this@DashboardActivity, DetailsEventActivity::class.java)
        intent.putExtra(DetailsEventActivity.EXTRA_EVENT,eventDataClass.event_id)
        startActivity(intent)
    }

    private fun getEvent(){
        val events : ArrayList<EventDataClass> = arrayListOf()
        fireStore.collection("Event")
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

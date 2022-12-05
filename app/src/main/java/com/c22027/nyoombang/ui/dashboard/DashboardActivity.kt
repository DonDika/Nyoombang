package com.c22027.nyoombang.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityDashboardBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var eventAdapter: EventAdapter
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

            }
        })
        binding.rvUsers.adapter =eventAdapter
    }

    private fun getEvent(){
        val event: ArrayList<EventDataClass> = arrayListOf()
        reference.get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val result = task.result
                result?.let {
                    it.children.map { snapshot ->
                        val eventData = EventDataClass(
                        snapshot.ref.key,
                        snapshot.child("user_id").value.toString(),
                        snapshot.child("eventName").value.toString(),
                        snapshot.child("eventPicture").value.toString(),
                        snapshot.child("eventDescription").value.toString(),
                        snapshot.child("endOfDate").value.toString(),
                        snapshot.child("totalAmount").value.toString().toInt()
                    )
                        event.add(eventData)

                    }
                }

            }
            eventAdapter.setData(event)

        }

    }


}
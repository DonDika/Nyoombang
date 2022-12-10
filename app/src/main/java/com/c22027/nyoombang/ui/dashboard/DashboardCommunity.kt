package com.c22027.nyoombang.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityDashboardCommunityBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.ui.addevent.AddEventActivity
import com.c22027.nyoombang.ui.details.DetailsEventActivity
import com.c22027.nyoombang.ui.profile.community.CommunityProfileActivity
import com.c22027.nyoombang.ui.profile.user.UserProfileActivity
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

        Log.d("uid",sharedPreferences.prefUid.toString())
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

    private fun showSelectedEvent(eventDataClass: EventDataClass){
        intent = Intent(this@DashboardCommunity, DetailsEventActivity::class.java)
        intent.putExtra(DetailsEventActivity.EXTRA_EVENT, eventDataClass)
        startActivity(intent)
    }


    private fun setupAdapter() {
        eventAdapter = EventAdapter(this@DashboardCommunity, arrayListOf(),
            object : EventAdapter.AdapterListener {
                override fun onClick(campaign: EventDataClass) {
                showSelectedEvent(campaign)
                }
            })
        binding.rvUsers.adapter = eventAdapter
    }


    private fun getEvent() {
        val events: ArrayList<EventDataClass> = arrayListOf()
        fireStore.collection("Event")
            .whereEqualTo("userId", sharedPreferences.prefUid.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    document.forEach {
                        val eventData = EventDataClass(
                            eventId = it.data["eventId"].toString(),
                            userId = it.data["userId"].toString(),
                            userName = it.data["userName"].toString(),
                            eventName = it.data["eventName"].toString(),
                            eventPicture = it.data["eventPicture"].toString(),
                            eventDescription = it.data["eventDescription"].toString(),
                            endOfDate = it.data["endOfDate"].toString(),
                            totalAmount = it.data["totalAmount"].toString().toInt(),
                            keyword = listOf(it.data["keyword"].toString())
                        )
                        events.add(eventData)
                    }

                }
                eventAdapter.setData(events)


            }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        intent = Intent(this@DashboardCommunity,CommunityProfileActivity::class.java)
        intent.putExtra(CommunityProfileActivity.EXTRA_ID,sharedPreferences.prefUid)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }

}
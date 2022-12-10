package com.c22027.nyoombang.ui.dashboard

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityDashboardBinding
import com.c22027.nyoombang.databinding.ActivityUserHistoryBinding
import com.c22027.nyoombang.databinding.ActivityUserProfileBinding
import com.c22027.nyoombang.ui.details.DetailsEventActivity
import com.c22027.nyoombang.ui.profile.user.UserHistoryActivity
import com.c22027.nyoombang.ui.profile.user.UserProfileActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore}
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

        binding.svSearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()){
                    val events : ArrayList<EventDataClass> = arrayListOf()
                    fireStore.collection("Event")
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
                }else {
                    val events: ArrayList<EventDataClass> = arrayListOf()
                    fireStore.collection("Event").whereArrayContains("keyword", query).get()
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
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    val events : ArrayList<EventDataClass> = arrayListOf()
                    fireStore.collection("Event")
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
                return true
            }

        } )
    }

    private fun setupAdapter() {
        eventAdapter = EventAdapter(this@DashboardActivity,arrayListOf(), object : EventAdapter.AdapterListener {
            override fun onClick(eventData: EventDataClass) {
                showSelectedEvent(eventData)

            }
        })
        binding.rvUsers.adapter = eventAdapter
    }

    private fun showSelectedEvent(eventDataClass: EventDataClass){
        intent = Intent(this@DashboardActivity, DetailsEventActivity::class.java)
        intent.putExtra(DetailsEventActivity.EXTRA_EVENT, eventDataClass)
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

        startActivity(Intent(this@DashboardActivity, UserProfileActivity::class.java))
        finish()
        return super.onOptionsItemSelected(item)
    }



}

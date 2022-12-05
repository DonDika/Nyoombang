package com.c22027.nyoombang.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.EventResponse
import com.c22027.nyoombang.data.model.UserDataClass
import com.c22027.nyoombang.data.model.UserResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppsRepositoryImpl {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val _toastObserverMessage = MutableLiveData<String>()
    val toastObserverMessage:MutableLiveData<String>   = _toastObserverMessage


    fun login(email: String, password: String): MutableLiveData<UserResponse> {
        val mutableLiveData = MutableLiveData<UserResponse>()
        database.child("UsersProfile").orderByChild("email").equalTo(email).get()
            .addOnCompleteListener { task ->
                val response = UserResponse()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.items = result.children.map { snapshot ->
                            snapshot.getValue(UserDataClass::class.java)!!
                        }
                    }

                } else {
                    response.exception = task.exception
                    Log.d("error", task.exception.toString())
                }
                mutableLiveData.value = response

            }
        return mutableLiveData

    }

    fun register(email: String):MutableLiveData<UserResponse> {
        val mutableLiveData = MutableLiveData<UserResponse>()
        database.child("UsersProfile").orderByChild("email").equalTo(email).get().addOnCompleteListener { task ->
            val response = UserResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.items = result.children.map { snapshot ->
                        snapshot.getValue(UserDataClass::class.java)!!
                    }
                }

            } else {
                response.exception = task.exception
                Log.d("error", task.exception.toString())
            }
            mutableLiveData.value = response

        }
        return mutableLiveData
    }

    fun getEventUser():MutableLiveData<EventResponse>{
        val mutableLiveData = MutableLiveData<EventResponse>()
        database.child("Event").get().addOnCompleteListener {task ->
            val response = EventResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.items = result.children.map { snapshot ->
                        snapshot.getValue(EventDataClass::class.java)!!
                    }
                }

            } else {
                response.exception = task.exception
                Log.d("error", task.exception.toString())
            }
            mutableLiveData.value = response

        }
        return mutableLiveData
    }
    fun getEventCommunity(userId: String):MutableLiveData<EventResponse>{
        val mutableLiveData = MutableLiveData<EventResponse>()
        database.child("Event").orderByChild("user_id").equalTo(userId).get().addOnCompleteListener {task ->
            val response = EventResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.items = result.children.map { snapshot ->
                        snapshot.getValue(EventDataClass::class.java)!!
                    }
                }

            } else {
                response.exception = task.exception
                Log.d("error", task.exception.toString())
            }
            mutableLiveData.value = response

        }
        return mutableLiveData
    }



}
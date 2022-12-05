package com.c22027.nyoombang.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.c22027.nyoombang.data.model.UserDataClass
import com.c22027.nyoombang.data.model.UserResponse
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppsRepositoryImpl {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getUser(id: String): LiveData<UserResponse> {
        val mutableData = MutableLiveData<UserResponse>()
        val data: LiveData<UserResponse> = mutableData
        database.child("UsersProfile").orderByChild("user_id").equalTo(id).get()
            .addOnCompleteListener { task ->
                val response = UserResponse()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.items = result.children.map { dataSnapshot ->
                            dataSnapshot.getValue(UserDataClass::class.java)!!
                        }
                    }
                } else {
                    response.exception = task.exception
                    Log.d(TAG, "getUser: ${task.exception.toString()}")
                }
                mutableData.value = response
            }
        return data
    }

    companion object {
        private val TAG = AppsRepositoryImpl::class.java.simpleName
    }
}
package com.c22027.nyoombang.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.data.local.UserResponse
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppsRepositoryImpl {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference



    fun login(email: String, password: String): MutableLiveData<UserResponse> {
        val mutableLiveData = MutableLiveData<UserResponse>()
        database.child("UsersProfile").orderByChild("email").equalTo(email).get().addOnCompleteListener{ task ->
            val response = UserResponse()
            if (task.isSuccessful){
                val result  = task.result
                result?.let {
                    response.items = result.children.map {snapshot ->
                        snapshot.getValue(UserDataClass::class.java)!!
                    }
                }

            }else{
                response.exception = task.exception
                Log.d("error",task.exception.toString())
            }
            mutableLiveData.value = response

        }
        return mutableLiveData

    }

}
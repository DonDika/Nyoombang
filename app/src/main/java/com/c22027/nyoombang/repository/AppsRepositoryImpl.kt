package com.c22027.nyoombang.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.c22027.nyoombang.data.model.*
import com.c22027.nyoombang.data.remote.ApiConfig
import com.c22027.nyoombang.data.remote.repository.AppsRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppsRepositoryImpl {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val _toastObserverMessage = MutableLiveData<String>()
    private val db: FirebaseFirestore = Firebase.firestore
    val toastObserverMessage:MutableLiveData<String>   = _toastObserverMessage
    private val _transaction = MutableLiveData<TransactionResponse>()
    val transaction: LiveData<TransactionResponse>   = _transaction



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
        database.child("Event").orderByChild("user_id").equalTo(userId)
            .get().addOnCompleteListener {task ->
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
                }
                mutableData.value = response
            }
        return data
    }

    fun getApi(orderId: String){
        val client = ApiConfig.getApiService().getTransaction(orderId)
        client.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("berhasil", "berhasil"+response.message())
                    _transaction.value = response.body()!!
                }


            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Log.d("Error",t.message.toString())
            }


        })

    }
}
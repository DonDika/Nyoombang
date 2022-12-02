package com.c22027.nyoombang.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.data.local.UserResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppsRepositoryImpl {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _firebaseUserData = MutableLiveData<FirebaseUser>()
    val firebaseUserData:MutableLiveData<FirebaseUser>   = _firebaseUserData
    private lateinit var reference: DatabaseReference
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

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUserData.value = (firebaseAuth.currentUser)
                    Log.d("success", it.result.toString())

                } else {
                    Log.d("error", it.exception.toString())
                    _toastObserverMessage.value = it.exception?.message.toString()

                }
            }



    }

    fun addUser(email: String, password: String, role: String, name: String) {
        val userId = firebaseAuth.currentUser!!.uid
        reference = FirebaseDatabase.getInstance().reference.child("UsersProfile")
        val userProfile = UserDataClass(
            userId,
            email,
            password,
            role,
            name,
            "",
            "",
            "",
            "",
            ""
        )
        reference.child(userId).setValue(userProfile).addOnCompleteListener{
            if (it.isSuccessful){
                _toastObserverMessage.value = "Registration Success"
            }else{
                _toastObserverMessage.value = it.exception?.message.toString()
            }

        }


    }


}
package com.c22027.nyoombang.detailsEvent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.data.model.TransactionResponse
import com.c22027.nyoombang.databinding.ActivityDetailsEventBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.utils.Utilization
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class DetailsEventActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailsEventBinding.inflate(layoutInflater) }
    private val ref by lazy { FirebaseDatabase.getInstance().reference }
    private val storage by lazy { FirebaseStorage.getInstance()}
    private val pref by lazy { SharedPreferencesHelper(this) }

    private lateinit var event: EventDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}
package com.c22027.nyoombang.ui.donation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.data.local.UserResponse
import com.c22027.nyoombang.databinding.ActivityDonationBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DonationActivity : AppCompatActivity(){


    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityDonationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper= SharedPreferencesHelper(this)
        reference = FirebaseDatabase.getInstance().reference
        binding.edit.setOnClickListener {
         intent = Intent(this@DonationActivity,EditDonationActivity::class.java)
         startActivity(intent)
        }
        displayData()
    }



    private fun displayData(){

        val user = sharedPreferencesHelper.prefUid.toString()
        reference.child("UsersProfile").child(user).get().addOnCompleteListener {

            if(it.isSuccessful){
                val snapshot = it.result
                val email = snapshot.child("email").value
                val name = snapshot.child("name").value
                val picture = snapshot.child("picture").value
                binding.tvEmail.text = email.toString()
                binding.tvName.text = name.toString()
                Glide.with(this)
                    .load(picture)
                    .into(binding.circleImageView)


        }
        }
    }
}

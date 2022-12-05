//package com.c22027.nyoombang.ui.donation
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.c22027.nyoombang.R
//import com.c22027.nyoombang.databinding.ActivityDonationHistoryBinding
//
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//
//class DonationHistoryActivity: AppCompatActivity()  {
//    private lateinit var reference: DatabaseReference
//    private lateinit var binding: ActivityDonationHistoryBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_donation)
//
//        reference = FirebaseDatabase.getInstance().reference
//    }
//
//
//
//    private fun displayData(){
//
//        reference.child("UsersProfile").get().addOnCompleteListener {
//            if (it.isSuccessful){
//
//            }
//        }
//    }
//}
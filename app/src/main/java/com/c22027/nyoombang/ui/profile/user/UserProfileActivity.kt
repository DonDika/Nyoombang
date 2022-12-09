package com.c22027.nyoombang.ui.profile.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityUserProfileBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.data.model.UserTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//import com.c22027.nyoombang.ui.adapter.DonationListAdapter

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var preferences: SharedPreferencesHelper
    private val db by lazy { Firebase.firestore }
    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var userHistoryAdapter: UserHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = SharedPreferencesHelper(this)
        setupView()

        binding.donation.setOnClickListener {
            intent = Intent(this@UserProfileActivity, UserHistoryActivity::class.java)
            startActivity(intent)
        }
        binding.edit.setOnClickListener {
            intent = Intent(this@UserProfileActivity, EditUserActivity::class.java)
            startActivity(intent)


        }
    }

    private fun setupView() {
        binding.apply {
            db.collection("UsersProfile").whereEqualTo("user_id", preferences.prefUid.toString())
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        document.forEach {
                            Glide.with(this@UserProfileActivity)
                                .load(it.data["picture"].toString())
                                .error(
                                    ContextCompat.getDrawable(
                                        this@UserProfileActivity,
                                        R.drawable.icon_account
                                    )
                                )
                                .into(civProfile)
                            edtName.text = it.data["name"].toString()
                            edtEmail.text = it.data["email"].toString()
                            edtPhoneNumber.text = it.data["phoneNumber"].toString()

                        }
                    }
                }

        }

}

}
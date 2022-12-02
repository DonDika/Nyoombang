package com.c22027.nyoombang.ui.profile.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityUserProfileBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper

//import com.c22027.nyoombang.ui.adapter.DonationListAdapter

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var preferences: SharedPreferencesHelper
    private val viewModel: UserProfileViewModel by viewModels()
//    private val adapter = DonationListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = SharedPreferencesHelper(this)

        setupView()
    }

    private fun setupView() {
        viewModel.getUser(preferences.prefUid.toString()).observe(this) {
            binding.apply {
                Glide.with(this@UserProfileActivity)
                    .load(it.items!![0].picture)
                    .error(ContextCompat.getDrawable(this@UserProfileActivity, R.drawable.icon_account))
                    .into(civProfile)

                edtName.setText(it.items!![0].name)
                edtEmail.setText(it.items!![0].email)
            }
        }

//        val donations = viewModel.getDonations()
//
//        val layoutManager = LinearLayoutManager(this)
//        binding.rvDonation.layoutManager = layoutManager
//        binding.rvDonation.setHasFixedSize(true)
//        binding.rvDonation.adapter = adapter
//
//        adapter.submitList(donations)
    }
}
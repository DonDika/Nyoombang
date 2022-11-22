package com.c22027.nyoombang.ui.profile.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.c22027.nyoombang.databinding.ActivityUserProfileBinding
import com.c22027.nyoombang.ui.adapter.DonationListAdapter

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel: UserProfileViewModel by viewModels()
    private val adapter = DonationListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val data = viewModel.getUser()
        binding.apply {
            Glide.with(this@UserProfileActivity)
                .load(data.image)
                .into(civProfile)

            edtName.setText(data.name)
            edtEmail.setText(data.email)
        }

        val donations = viewModel.getDonations()

        val layoutManager = LinearLayoutManager(this)
        binding.rvDonation.layoutManager = layoutManager
        binding.rvDonation.setHasFixedSize(true)
        binding.rvDonation.adapter = adapter

        adapter.submitList(donations)
    }
}
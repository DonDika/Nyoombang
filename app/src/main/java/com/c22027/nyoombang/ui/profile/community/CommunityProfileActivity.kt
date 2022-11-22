package com.c22027.nyoombang.ui.profile.community

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.c22027.nyoombang.databinding.ActivityCommunityProfileBinding

class CommunityProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityProfileBinding
    private val viewModel: CommunityProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val data = viewModel.getCommunity()
        binding.apply {
            Glide.with(this@CommunityProfileActivity)
                .load(data.image)
                .into(civProfile)

            edtName.setText(data.name)
            edtEmail.setText(data.email)
            edtDescription.setText(data.description)

            if (data.facebook != null) {
                ibFacebook.setOnClickListener {

                }
            } else {
                ibFacebook.visibility = View.GONE
            }

            if (data.instagram != null) {
                ibInstagram.setOnClickListener {

                }
            } else {
                ibInstagram.visibility = View.GONE
            }

            if (data.twitter != null) {
                ibTwitter.setOnClickListener {

                }
            } else {
                ibTwitter.visibility = View.GONE
            }
        }
    }
}
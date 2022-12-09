package com.c22027.nyoombang.ui.details

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityCommunityDetailsBinding
import com.c22027.nyoombang.ui.profile.community.CommunityProfileViewModel

class CommunityDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityDetailsBinding
    private val viewModel: CommunityProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupView()
    }

    private fun setupView() {
        val communityId = intent.getStringExtra(EXTRA_USER).toString()
        viewModel.getCommunity(communityId).observe(this) {
            binding.apply {
                Glide.with(this@CommunityDetailsActivity)
                    .load(it.items!![0].picture)
                    .error(ContextCompat.getDrawable(this@CommunityDetailsActivity, R.drawable.icon_account))
                    .into(civProfile)

                edtName.setText(it.items!![0].name)
                edtEmail.setText(it.items!![0].email)
                edtDescription.setText(it.items!![0].description)

                if (it.items!![0].facebook != null || it.items!![0].facebook == "") {
                    ibFacebook.setOnClickListener {

                    }
                } else {
                    ibFacebook.visibility = View.GONE
                }

                if (it.items!![0].instagram != null || it.items!![0].instagram == "") {
                    ibInstagram.setOnClickListener {

                    }
                } else {
                    ibInstagram.visibility = View.GONE
                }

                if (it.items!![0].twitter != null || it.items!![0].twitter == "") {
                    ibTwitter.setOnClickListener {

                    }
                } else {
                    ibTwitter.visibility = View.GONE
                }
            }
        }
    }

    companion object{
        const val EXTRA_USER = "extra user"
    }


}
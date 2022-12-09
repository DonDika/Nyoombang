package com.c22027.nyoombang.ui.profile.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityCommunityProfileBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityProfileBinding
    private lateinit var preferences: SharedPreferencesHelper
    private val viewModel: CommunityProfileViewModel by viewModels()
    private val db by lazy { Firebase.firestore}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = SharedPreferencesHelper(this)

        setupView()
        binding.edit.setOnClickListener {
            intent  = Intent(this@CommunityProfileActivity,EditCommunityActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupView() {
        binding.apply {
            db.collection("UsersProfile").whereEqualTo("user_id", preferences.prefUid.toString())
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        document.forEach {
                            Glide.with(this@CommunityProfileActivity)
                                .load(it.data["picture"].toString())
                                .error(
                                    ContextCompat.getDrawable(
                                        this@CommunityProfileActivity,
                                        R.drawable.icon_account
                                    )
                                )
                                .into(civProfile)
                            edtName.setText(it.data["name"].toString())
                            edtEmail.setText(it.data["email"].toString())
                            edtDescription.setText(it.data["description"].toString())

                            if (it.data["facebook"].toString() != null || it.data["facebook"].toString() == "") {
                                ibFacebook.setOnClickListener {

                                }
                            } else {
                                ibFacebook.visibility = View.GONE
                            }

                            if (it.data["instagram"].toString() != null || it.data["instagram"].toString() == "") {
                                ibInstagram.setOnClickListener {

                                }
                            } else {
                                ibInstagram.visibility = View.GONE
                            }

                            if (it.data["twitter"].toString() != null || it.data["twitter"].toString() == "") {
                                ibTwitter.setOnClickListener {

                                }
                            } else {
                                ibTwitter.visibility = View.GONE
                            }
                        }
                    }
                }
        }

//        viewModel.getCommunity(preferences.prefUid.toString()).observe(this) {
//            binding.apply {
//                Glide.with(this@CommunityProfileActivity)
//                    .load(it.items!![0].picture)
//                    .error(ContextCompat.getDrawable(this@CommunityProfileActivity, R.drawable.icon_account))
//                    .into(civProfile)
//
//                edtName.setText(it.items!![0].name)
//                edtEmail.setText(it.items!![0].email)
//                edtDescription.setText(it.items!![0].description)
//
//                if (it.items!![0].facebook != null || it.items!![0].facebook == "") {
//                    ibFacebook.setOnClickListener {
//
//                    }
//                } else {
//                    ibFacebook.visibility = View.GONE
//                }
//
//                if (it.items!![0].instagram != null || it.items!![0].instagram == "") {
//                    ibInstagram.setOnClickListener {
//
//                    }
//                } else {
//                    ibInstagram.visibility = View.GONE
//                }
//
//                if (it.items!![0].twitter != null || it.items!![0].twitter == "") {
//                    ibTwitter.setOnClickListener {
//
//                    }
//                } else {
//                    ibTwitter.visibility = View.GONE
//                }
//            }
//        }
    }


}
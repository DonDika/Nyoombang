package com.c22027.nyoombang.ui.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.R
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.ui.auth.login.LoginActivity
import com.c22027.nyoombang.ui.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {


    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferencesHelper= SharedPreferencesHelper(this)
        setupView()
        init()
    }

    private fun init(){

        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPreferencesHelper.prefNewAccess){
                val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
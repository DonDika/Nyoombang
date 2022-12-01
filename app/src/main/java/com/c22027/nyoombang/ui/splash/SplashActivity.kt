package com.c22027.nyoombang.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c22027.nyoombang.R
import android.os.Handler
import android.os.Looper
import com.c22027.nyoombang.ui.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()
    }

    private fun init(){
        Handler(Looper.getMainLooper()).postDelayed({
//            if (token.isNullOrEmpty()){
//                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }else{
                val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
//            }
        }, 3000)
    }

}
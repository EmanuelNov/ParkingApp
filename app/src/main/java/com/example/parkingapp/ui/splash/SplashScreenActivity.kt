package com.example.parkingapp.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.parkingapp.ui.home.MainActivity
import com.example.parkingapp.databinding.ActivitySplashBinding

class SplashScreenActivity:Activity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            val i =Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        },300)
    }

}
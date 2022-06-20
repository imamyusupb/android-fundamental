package com.codeyuba.submisionbfaaone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codeyuba.submisionbfaaone.R
import com.codeyuba.submisionbfaaone.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding :ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this,MainActivity::class.java))
        },3000)

        Glide.with(this@SplashScreenActivity.baseContext)
            .load(R.drawable.octocat)
            .apply(RequestOptions.overrideOf(150,150))
            .into(binding.imgSplashScreen)
    }

}
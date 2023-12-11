package com.example.cleanair

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanair.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val animationDrawable = binding.mainLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(50)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }
}
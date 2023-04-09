package com.emreeldemir.leafy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emreeldemir.leafy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle click and Login
        binding.loginButton.setOnClickListener {
            TODO()
        }

        // Handle click, Skip and go to Home Screen
        binding.skipButton.setOnClickListener {
            TODO()
        }
    }
}
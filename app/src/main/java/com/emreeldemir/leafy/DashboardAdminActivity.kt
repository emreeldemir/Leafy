package com.emreeldemir.leafy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emreeldemir.leafy.databinding.ActivityDashboardAdminBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardAdminActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityDashboardAdminBinding

    /**
     * Firebase Authentication
     */
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Init Firebase Auth
         */
        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        /**
         * Handle Click, Logout
         */
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }

    private fun checkUser() {
        /**
         * Get Current User
         */
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            // User not logged in, go to Main Page
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else {
            // User is logged in, get and show user info
            val email = firebaseUser.email
            // Set textView in Dashboard
            binding.subtitleTextView.text = email
        }
    }
}
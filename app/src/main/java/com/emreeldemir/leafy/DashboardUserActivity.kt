package com.emreeldemir.leafy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emreeldemir.leafy.databinding.ActivityDashboardUserBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardUserActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityDashboardUserBinding

    /**
     * Firebase Authentication
     */
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun checkUser() {
        /**
         * Get Current User
         */
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            /**
             * User not logged in,
             * The user can stay in the DashboardUserActivity
             * without login
             */
            binding.subtitleTextView.text = "You are not logged in"

        }

        else {
            // User is logged in, get and show user info
            val email = firebaseUser.email
            // Set textView in Dashboard
            binding.subtitleTextView.text = email
        }
    }
}
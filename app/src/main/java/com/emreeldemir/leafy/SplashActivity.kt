package com.emreeldemir.leafy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // 1.75 seconds delay and then go to Main Page
        Handler().postDelayed(Runnable {
            checkUser()
        }, 1750) // it means 1.75 seconds

    }

    private fun checkUser() {
        // Check if user is logged in or not
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            // User not logged in, go to Main Page
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else {
            // User is logged in, check user type (SAME in Login Page)
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Get user type - User or Admin
                        val userType = snapshot.child("userType").value

                        if (userType == "user") {
                            // User is logged in, start User Dashboard
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()
                        }

                        else if (userType == "admin") {
                            // Admin is logged in, start Admin Dashboard
                            startActivity(Intent(this@SplashActivity, DashboardAdminActivity::class.java))
                            finish()
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {


                    }

                })

        }
    }
}
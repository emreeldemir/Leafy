package com.emreeldemir.leafy

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.emreeldemir.leafy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityLoginBinding

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Init Progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        // Handle Click, Not have account, go to register screen
        binding.noAccountTextView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Handle Click, Start Login
        binding.loginButton.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        // Get (Input) Data
        email = binding.editTextEmail.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()

        // Validate Data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
        } else {
            loginUser()
        }


    }

    private fun loginUser() {
        // Show Progress Dialog
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        // Login using firebase
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Login Success
                progressDialog.dismiss()
                // Get User Info
                    checkUser()

            }
            .addOnFailureListener { e ->
                // Login Failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkUser() {
        // If User - Start User Dashboard
        // If Admin - Start Admin Dashboard
        progressDialog.setMessage("Checking User...")

        val firebaseUser = firebaseAuth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    // Get user type - User or Admin
                    val userType = snapshot.child("userType").value

                    if (userType == "user") {
                        // User is logged in, start User Dashboard
                        startActivity(Intent(this@LoginActivity, DashboardUserActivity::class.java))
                        finish()
                    } else if(userType == "admin") {
                        // Admin is logged in, start Admin Dashboard
                        startActivity(Intent(this@LoginActivity, DashboardAdminActivity::class.java))
                        finish()
                    }


                }

                override fun onCancelled(error: DatabaseError) {


                }

            })

    }


}
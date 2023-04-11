package com.emreeldemir.leafy

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.emreeldemir.leafy.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivitySignUpBinding

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    private var name = ""
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Init Progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        // Handle Click, Start Register User

        binding.signUpButton.setOnClickListener {
            validateData()
        }


    }

    private fun validateData() {
        // Get (Input) Data
        name = binding.editTextName.text.toString().trim()
        email = binding.editTextEmail.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()
        val cPassword = binding.editTextCPassword.text.toString().trim()

        // Validate Data
        if (name.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please Enter The Password", Toast.LENGTH_SHORT).show()
        } else if (cPassword.isEmpty()) {
            Toast.makeText(this, "Please Confirm The Password", Toast.LENGTH_SHORT).show()
        } else if (password != cPassword) {
            Toast.makeText(this, "Password Doesn't Match", Toast.LENGTH_SHORT).show()
        } else {
            createUserAccount()
        }


    }

    private fun createUserAccount() {
        // Show Progress Dialog
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        // Create user in Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Add user info to Firebase Realtime Database
                updateUserInfo()
            }
            .addOnFailureListener { e ->
                // Failed to create account
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Create Account: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Saving User Info...")

        // Timestamp
        val timestamp = System.currentTimeMillis()

        // Get uid of current user
        val uid = firebaseAuth.uid

        // Setup data to save
        val hashMap = HashMap<String, Any?>()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        // Save to Firebase Realtime Database
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!).setValue(hashMap)
            .addOnSuccessListener {
                // User info saved, open user dashboard
                progressDialog.dismiss()
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SignUpActivity, DashboardUserActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // Failed to save user info
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Save User Info: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }


    }


}
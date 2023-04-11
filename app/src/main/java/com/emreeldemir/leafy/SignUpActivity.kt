package com.emreeldemir.leafy

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.emreeldemir.leafy.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

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
        progressDialog.setTitle("Please wait")
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
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email pattern", Toast.LENGTH_SHORT).show()
        }

        else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
        }

        else if (cPassword.isEmpty()) {
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show()
        }

        else if (password != cPassword) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
        }

        else {
            createUserAccount()
        }





    }

    private fun createUserAccount() {

    }


}
package com.emreeldemir.leafy

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.emreeldemir.leafy.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityForgotPasswordBinding

    /**
     * Firebase Auth
     */
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Progress Dialog
     */
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Init Firebase Auth
         */
        firebaseAuth = FirebaseAuth.getInstance()

        /**
         * Init Progress Dialog
         */
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)


        /**
         * Handle Click, Begin Reset Password
         */
        binding.submitButton.setOnClickListener{
            validateData()
        }



    }

    private var email = ""
    private fun validateData() {
        // Get data
        email = binding.editTextEmail.text.toString().trim()

        // Validate data
        if (email.isEmpty()){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
        }
        else {
            resetPassword()
        }

    }

    private fun resetPassword() {
        // Show progress
        progressDialog.setMessage("Sending instructions to $email")
        progressDialog.show()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                // Email sent
                progressDialog.dismiss()
                Toast.makeText(this, "Instructions sent to \n$email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Failed
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to send due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
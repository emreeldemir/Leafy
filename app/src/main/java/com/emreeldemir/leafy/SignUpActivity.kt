package com.emreeldemir.leafy

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emreeldemir.leafy.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivitySignUpBinding

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog
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


    }
}
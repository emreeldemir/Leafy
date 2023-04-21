package com.emreeldemir.leafy

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.emreeldemir.leafy.databinding.ActivityCategoryAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryAddActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityCategoryAddBinding

    /**
     * Firebase Auth
     */
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Progress Dialog
     */
    private lateinit var progressDialog: ProgressDialog

    private var category = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Init Firebase Auth
         */
        firebaseAuth = FirebaseAuth.getInstance()

        /**
         * Init Progress Dialog
         */
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        /**
         * Handle Click go Back
         */
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        /**
         * Handle Click Begin Upload Category
         */
        binding.submitButton.setOnClickListener {
            validateData()
        }


    }

    private fun validateData() {
        /**
         * Get Data
         */
        category = binding.categoryEditText.text.toString().trim()

        /**
         * Validate Data
         */
        if (category.isEmpty()) {
            Toast.makeText(this, "Please Enter Category", Toast.LENGTH_SHORT).show()
        } else {
            addCategoryFirebase()
        }


    }

    private fun addCategoryFirebase() {
        /**
         * Show Progress Dialog
         */
        progressDialog.show()

        /**
         * Get timestamp
         */
        val timestamp = System.currentTimeMillis()

        /**
         * Setup Data to Add in Firebase Database
         */
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        /**
         * Add Data to Firebase Database
         * Root > Categories > Category ID > Category Info
         */
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp").setValue(hashMap)
            .addOnSuccessListener {
                /**
                 * Added Successfully
                 * Dismiss Progress Dialog
                 */
                progressDialog.dismiss()
                Toast.makeText(this, "Category Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                /**
                 * Failed to Add
                 * Dismiss Progress Dialog
                 */
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Add due to: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }


}
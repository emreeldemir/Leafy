package com.emreeldemir.leafy

import android.R
import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.emreeldemir.leafy.databinding.ActivityPdfAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PdfAddActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityPdfAddBinding

    /**
     * Firebase Authentication
     */
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Progress Dialog
     */
    private lateinit var progressDialog: ProgressDialog

    /**
     * ArrayList to Hold PDF Categories
     */
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    /**
     * URI of Picked PDF
     */
    private var pdfUri: Uri? = null

    /**
     * TAG
     */
    private val TAG = "PDF_ADD_TAG"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         * Firebase Init
         */
        firebaseAuth = FirebaseAuth.getInstance()
        loadPdfCategories()

        /**
         * Progress Dialog Setup
         */
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        /**
         * Handle Click, Show Category Pick Dialog
         */
        binding.categoryTv.setOnClickListener {
            categoryPickDialog()
        }



    }

    private fun loadPdfCategories() {
        Log.d(TAG, "loadPdfCategories: Loading PDF Categories")

        // Init ArrayList
        categoryArrayList = ArrayList()

        // Database Reference to Load Categories
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear List Before Adding Data
                categoryArrayList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: ${error.message}")
            }

        })


    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""
    private fun categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: Showing Dialog")

        // Get String Array of Categories from ArrayList
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)

        for (i in categoryArrayList.indices){
            categoriesArray[i] = categoryArrayList[i].category
        }

        // Alert Dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray) { dialog, which ->
                // Handle Item Click
                selectedCategoryTitle = categoryArrayList[which].category
                selectedCategoryId = categoryArrayList[which].id
                binding.categoryTv.text = selectedCategoryTitle

                Log.d(TAG, "categoryPickDialog: Selected Category ID: $selectedCategoryId")
                Log.d(TAG, "categoryPickDialog: Selected Category Title: $selectedCategoryTitle")
            }
            .show()

    }

    private fun pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: Starting PDF Pick Intent")

        // Intent to Pick PDF Files
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)


    }

    val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "PDF Picked")
                pdfUri = result.data!!.data
            }
            else {
                Log.d(TAG, "PDF Pick cancelled")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    )


}
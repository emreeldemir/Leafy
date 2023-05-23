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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

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
         * Handle Click, Go Back
         */
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        /**
         * Handle Click, Show Category Pick Dialog
         */
        binding.categoryTv.setOnClickListener {
            categoryPickDialog()
        }

        /**
         * Handle Click, Pick PDF Intent
         */
        binding.attachPdfButton.setOnClickListener {
            pdfPickIntent()
        }

        /**
         * Handle Click, Start Upload PDF
         */
        binding.submitButton.setOnClickListener {
            validateData()
        }


    }

    private var title = ""
    private var description = ""
    private var category = ""
    private fun validateData() {
        // Validate Data
        Log.d(TAG, "validateData: Validating Data")

        // Get Data
        title = binding.titleEt.text.toString().trim()
        description = binding.descriptionEt.text.toString().trim()
        category = binding.categoryTv.text.toString().trim()

        // Validate Data
        if (title.isEmpty()){
            Toast.makeText(this, "Enter Title...", Toast.LENGTH_SHORT).show()
        }
        else if (description.isEmpty()){
            Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show()
        }
        else if (category.isEmpty()){
            Toast.makeText(this, "Pick Category...", Toast.LENGTH_SHORT).show()
        }
        else if (pdfUri == null){
            Toast.makeText(this, "Pick PDF File...", Toast.LENGTH_SHORT).show()
        }
        else{
            // All Data is Valid, Start Upload
            uploadPdfToStorage()
        }

    }

    private fun uploadPdfInfoToDb(uploadedPdfUrl: String, timestamp: String) {
        // Upload PDF Info to Firebase Database
        Log.d(TAG, "uploadPdfInfoToDb: Uploading to Database")

        // Show Progress
        progressDialog.setMessage("Uploading PDF Info...")
        progressDialog.show()

        val uid = firebaseAuth.uid

        // Setup Data to Upload
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"
        hashMap["url"] = "$uploadedPdfUrl"
        hashMap["timestamp"] = "$timestamp"

        // DB Reference DB > Books > BookId > (Book Info)
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                // Uploaded to DB
                Log.d(TAG, "uploadPdfInfoToDb: Uploaded to DB")
                progressDialog.dismiss()
                Toast.makeText(this, "Uploaded...", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener { e ->
                // Failed to Upload to DB
                Log.d(TAG, "uploadPdfInfoToDb: Failed to Upload to DB due to ${e.message}")
                Toast.makeText(this, "Failed to Upload to DB due to ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun uploadPdfToStorage() {
        Log.d(TAG, "uploadPdfToStorage: Uploading PDF to Storage")

        // Show Progress
        progressDialog.setMessage("Uploading PDF...")
        progressDialog.show()

        // Timestamp
        val timestamp = "" + System.currentTimeMillis()

        // Path of PDF in Firebase Storage
        val filePathAndName = "Books/$timestamp"

        // Storage Reference
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)

        // Upload PDF
        storageReference.putFile(pdfUri!!)

            .addOnSuccessListener { taskSnapshot ->
                // PDF Uploaded, Get URL
                Log.d(TAG, "uploadPdfToStorage: PDF Uploaded Now getting URL...")

                // Get URL of Uploaded PDF
                val uriTask: Task<Uri> taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedPdfUrl = "${uriTask.result}"

               uploadPdfInfoToDb(uploadedPdfUrl, timestamp)

            }
            .addOnFailureListener { e ->
                // PDF Upload Failed
                Log.d(TAG, "uploadPdfToStorage: failed to upload due to  ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload due to  ${e.message}", Toast.LENGTH_SHORT).show()
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
package com.emreeldemir.leafy

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emreeldemir.leafy.databinding.ActivityPdfAddBinding
import com.google.firebase.auth.FirebaseAuth

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


        /**
         * Progress Dialog Setup
         */
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)



    }
}
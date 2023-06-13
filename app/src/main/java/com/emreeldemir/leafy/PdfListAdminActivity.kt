package com.emreeldemir.leafy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PdfListAdminActivity : AppCompatActivity() {

    /**
     * Category ID, Title
     */
    private var categoryId = ""
    private var category = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_list_admin)

        // Get category id and category title from intent
        val intent = intent
        categoryId = intent.getStringExtra("categoryId")!!
        category = intent.getStringExtra("category")!!

    }
}
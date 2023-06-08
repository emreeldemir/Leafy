package com.emreeldemir.leafy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.emreeldemir.leafy.databinding.ActivityDashboardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardAdminActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityDashboardAdminBinding

    /**
     * Firebase Authentication
     */
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * ArrayList to Hold Categories
     */
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    /**
     * Adapter
     */
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Init Firebase Auth
         */
        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        loadCategories()

        /**
         * Search
         */
        binding.searchEditText.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Called when text changed
                try {
                    adapterCategory.filter.filter(s)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        /**
         * Handle Click, Logout
         */
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        /**
         * Handle Click, Start Add Category Page
         */
        binding.addCategoryButton.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity::class.java))
        }

        /**
         * Handle Click, Start Add PDF Page
         */
        binding.addPdfFab.setOnClickListener {
            startActivity(Intent(this, PdfAddActivity::class.java))

        }


        /**
         * Handle Click, Open Profile
         */
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }





    }

    private fun loadCategories() {
        /**
         * Init ArrayList
         */
        categoryArrayList = ArrayList()

        /**
         * Get All Categories from Firebase ... Firebase DB > Categories
         */
        val ref = FirebaseDatabase.getInstance().getReference("Categories")

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
            // Clear list before adding new data in it
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    // Get data
                    val model = ds.getValue(ModelCategory::class.java)

                    // Add to ArrayList
                    categoryArrayList.add(model!!)
                }

                // Setup Adapter
                adapterCategory = AdapterCategory(this@DashboardAdminActivity, categoryArrayList)

                // Set Adapter to RecyclerView
                binding.categoriesRv.adapter = adapterCategory

            }

            override fun onCancelled(error: DatabaseError) {


            }

        })


    }

    private fun checkUser() {
        /**
         * Get Current User
         */
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            // User not logged in, go to Main Page
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else {
            // User is logged in, get and show user info
            val email = firebaseUser.email
            // Set textView in Dashboard
            binding.subtitleTextView.text = email
        }
    }
}
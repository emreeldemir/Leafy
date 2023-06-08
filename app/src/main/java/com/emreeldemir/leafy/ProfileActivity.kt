package com.emreeldemir.leafy

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.emreeldemir.leafy.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityProfileBinding

    /**
     * Firebase Auth
     */
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         * Init Firebase Auth
         */
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        /**
         * Handle Click, Go Back
         */
        binding.backButton.setOnClickListener {
            onBackPressed()
        }


        /**
         * Handle Click, Open Edit Profile
         */
        binding.profileEditButton.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }



    }

    private fun loadUserInfo() {
        // DB Reference to Load User Info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Get User Info
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val userType = "${snapshot.child("userType").value}"

                    // Convert timestamp to Proper Format
                    val formattedDate = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, Locale.getDefault()).format(timestamp.toLong())

                    // Set Data
                    binding.nameTv.text = name
                    binding.emailTv.text = email
                    binding.memberDateTv.text = formattedDate
                    binding.accountTypeTv.text = userType

                    // Set Image
                    try {
                        Glide.with(this@ProfileActivity)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_person_gray)
                            .into(binding.profileIv)
                    } catch (e: Exception) {

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }


}
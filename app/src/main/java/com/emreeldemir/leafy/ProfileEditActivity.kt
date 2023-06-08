package com.emreeldemir.leafy

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.emreeldemir.leafy.databinding.ActivityProfileEditBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileEditActivity : AppCompatActivity() {

    /**
     * View Binding
     */
    private lateinit var binding: ActivityProfileEditBinding

    /**
     * Firebase Auth
     */
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Image URI (Which we will pick)
     */
    private var imageUri : Uri? = null

    /**
     * Progress Dialog
     */
    private lateinit var progressDialog: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Init Progress Dialog
         */
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        /**
         * Init Firebase Auth
         */
        firebaseAuth = FirebaseAuth.getInstance()

        loadUserInfo()

        /**
         * Handle Click, Go Back
         */
        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        /**
         * Handle Click, Pick Image from Camera or Gallery
         */
        binding.profileIv.setOnClickListener{
            showImageAttachMenu()
        }

        /**
         * Handle Click, Begin Update Profile
         */
        binding.updateButton.setOnClickListener{
            validateData()
        }



    }

    private var name = ""
    private fun validateData() {
        // Get Data
        name = binding.nameEt.text.toString().trim()

        // Validate Data
        if (name.isEmpty()){
            Toast.makeText(this, "Please enter name...", Toast.LENGTH_SHORT).show()
        }
        else{

            if(imageUri == null){
                updateProfile("")
            }
            else {
                uploadImage()
            }
        }

    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading Profile Image")
        progressDialog.show()

        val filePathAndName = "ProfileImages/"+firebaseAuth.uid

        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val uploadedImageUrl = "${ uriTask.result }"

                updateProfile(uploadedImageUrl)

            }

            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadedImageUrl: String) {
        progressDialog.setMessage("Updating Profile...")

        val hashmap: HashMap<String, Any> = HashMap()
        hashmap["name"] = "$name"
        if (imageUri != null){
            hashmap["profileImage"] = uploadedImageUrl
        }

        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashmap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()

            }

            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to update profile due to ${e.message}", Toast.LENGTH_SHORT).show()

            }


    }

    private fun loadUserInfo() {
        // DB Reference to Load User Info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Get User Info
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"


                    // Set Data
                    binding.nameEt.setText(name)

                    // Set Image
                    try {
                        Glide.with(this@ProfileEditActivity)
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


    private fun showImageAttachMenu(){
        val popupMenu = PopupMenu(this, binding.profileIv)
        popupMenu.menu.add(Menu.NONE, 0, 0,"Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1,"Gallery")
        popupMenu.show()

        // Handle Click
        popupMenu.setOnMenuItemClickListener { item ->

            val id = item.itemId

            if (id == 0){
                // Camera Clicked

            }
            else if (id == 1){
                // Gallery Clicked
                pickImageGallery()
            }


            true
        }

    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)

    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback<ActivityResult> {result ->  
            // Get URI of Image
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data
                // Set to ImageView
                binding.profileIv.setImageURI(imageUri)
            }
            else {
                Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )

}
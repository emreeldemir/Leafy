package com.emreeldemir.leafy

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emreeldemir.leafy.databinding.RowCategoryBinding
import com.google.firebase.database.FirebaseDatabase

class AdapterCategory: RecyclerView.Adapter<AdapterCategory.HolderCategory>, Filterable {

    private val context: Context
    public var categoryArrayList: ArrayList<ModelCategory>
    private var filterList: ArrayList<ModelCategory>

    private var filter: FilterCategory? = null

    private lateinit var binding: RowCategoryBinding


    /**
     * Constructor
     */
    constructor(context: Context, categoryArrayList: ArrayList<ModelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        // Inflate/Bind row_category.xml
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        /**
         * Get Data, Set Data, Handle View clicks in this method
         */


        // Get data
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        // Set data
        holder.categoryTextView.text = category

        // Handle click, Delete Category
        holder.deleteButton.setOnClickListener {

            // Confirm before delete
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this category?")

                .setPositiveButton("Confirm") {a, d->
                    Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                    deleteCategory(model, holder)
                }

                .setNegativeButton("Cancel") {a, d->
                    a.dismiss()
                }
                .show()
        }

    }


    override fun getItemCount(): Int {
        // Number of items in the list
        return categoryArrayList.size
    }


    private fun deleteCategory(model: ModelCategory, holder: HolderCategory) {
        /**
         * Get ID of the category to delete
         */
        val id = model.id

        // Firebase DB > Categories > CategoryID
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                // Category deleted
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }


    /**
     * View Holder Class to hold or init UI Views for row_category.xml
     */
    inner class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Init UI Views
        val categoryTextView: TextView = binding.categoryTextView
        val deleteButton: ImageButton = binding.deleteButton
    }

    override fun getFilter(): Filter {
        if(filter == null) {
            filter = FilterCategory(filterList, this)
        }
        return filter as FilterCategory
    }


}
package com.emreeldemir.leafy

import android.widget.Filter

class FilterCategory: Filter {

    /**
     * ArrayList in which we want to search
     */
    private var filterList: ArrayList<ModelCategory>

    /**
     * Adapter where we want to set filtered data
     */
    private var adapterCategory: AdapterCategory

    /**
     * Constructor
     */
    constructor(filterList: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) : super() {
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        // Value shouldn't be null and empty
        if(constraint != null && constraint.isEmpty()) {

            // Avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModel: ArrayList<ModelCategory> = ArrayList()

            for (i in 0 until filterList.size) {

                // Validate
                if(filterList[i].category.uppercase().contains(constraint)) {
                    // Add to filtered list
                    filteredModel.add(filterList[i])
                }
            }


        }

    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        TODO("Not yet implemented")
    }


}
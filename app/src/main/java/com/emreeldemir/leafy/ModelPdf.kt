package com.emreeldemir.leafy

class ModelPdf {

    // Variables
    var uid: String = ""
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var categoryId: String = ""
    var url: String = ""
    var timestamp: Long = 0


    // Empty constructor (required by Firebase)
    constructor()

    // Constructor with parameters
    constructor(
        uid: String,
        id: String,
        title: String,
        description: String,
        categoryId: String,
        url: String,
        timestamp: Long
    ) {
        this.uid = uid
        this.id = id
        this.title = title
        this.description = description
        this.categoryId = categoryId
        this.url = url
        this.timestamp = timestamp
    }


}
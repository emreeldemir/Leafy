package com.emreeldemir.leafy

class ModelCategory {

    /**
     * Variables should be match with the variables in the Firebase
     */

    var id: String = ""
    var category: String = ""
    var timestamp: Long = 0
    var uid: String = ""

    /**
     * Empty constructor is needed for Firebase
     */
    constructor()

    /**
     * Constructor with parameters
     */
    constructor(id: String, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }


}
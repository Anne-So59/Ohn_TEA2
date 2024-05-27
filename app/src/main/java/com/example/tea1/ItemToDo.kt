package com.example.tea1

class ItemToDo {

    // attributs
    var description: String
    var fait: Boolean

    // constructeurs
    constructor() {
        this.description = ""
        this.fait = false
    }

    constructor(description: String) {
        this.description = description
        this.fait = false
    }

    constructor(description: String, fait: Boolean) {
        this.description = description
        this.fait = fait
    }

    // getters et setters automatiques en Kotlin

    // méthode toString
    override fun toString(): String {
        return "ItemToDo(description='$description', fait=$fait)"
    }
}
package com.example.tea1

class ProfilListeToDo {

    // attributs
    var login: String = ""
    var mesListesToDo: MutableList<ListeToDo> = mutableListOf()

    // constructeurs
    constructor()

    constructor(login: String, mesListesToDo: List<ListeToDo>) {
        this.login = login
        this.mesListesToDo.addAll(mesListesToDo)
    }

    constructor(mesListesToDo: List<ListeToDo>) {
        this.mesListesToDo.addAll(mesListesToDo)
    }

    // getters et setters automatiques en Kotlin

    // ajout d'une liste
    fun ajouteListe(uneListe: ListeToDo) {
        this.mesListesToDo.add(uneListe)
    }

    // méthode toString
    override fun toString(): String {
        return "ProfilListeToDo(login='$login', mesListesToDo=$mesListesToDo)"
    }
}
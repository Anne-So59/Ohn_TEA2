package com.example.tea1

class ListeToDo {

    // attributs
    var titreListeToDo: String = ""
    var lesItems: MutableList<ItemToDo> = mutableListOf()

    // constructeur par défaut
    constructor()

    // getters et setters automatiques en Kotlin, donc on change le nom pour définir notre propre setter
    fun changerLesItems(items: List<ItemToDo>) {
        lesItems.clear()
        lesItems.addAll(items)
    }

    // recherche d'un item
    fun rechercherItem(descriptionItem: String): ItemToDo? {
        return lesItems.find { it.description == descriptionItem }
    }

    // méthode toString
    override fun toString(): String {
        return "ListeToDo(titreListeToDo='$titreListeToDo', lesItems=$lesItems)"
    }
}
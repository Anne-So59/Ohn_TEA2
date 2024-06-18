package com.example.tea1

class ItemItem(
    val id: String = "",
    val label: String = "",
    val url: String = "",
    val checked: String = ""
)

data class ItemDataClass(
    val version: Double = 1.2,
    var success: Boolean = false,
    var status: Int = 0,
    var apiname: String = "todo",
    var items: List<ItemItem> = emptyList()
)
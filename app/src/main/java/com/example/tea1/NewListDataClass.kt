package com.example.tea1

data class NewListDataClass(
    val version: Double = 1.2,
    var success: Boolean = false,
    var status: Int = 0,
    var apiname: String = "todo",
    var list: List<ListList> = emptyList()
)

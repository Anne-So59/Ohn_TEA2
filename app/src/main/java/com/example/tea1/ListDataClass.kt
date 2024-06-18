package com.example.tea1

class ListList(
    val id:String="",
    val label:String=""
)

data class ListDataClass(
    val version: Double = 1.2,
    var success: Boolean = false,
    var status: Int = 0,
    var apiname: String = "todo",
    var lists: List<ListList> = emptyList()
)
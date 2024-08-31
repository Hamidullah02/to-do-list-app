package com.learning.shoppinglist

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase

fun main() {
    val name : String? = "ella"
    name?.let {
        println(it.toUpperCase())
    }
    println(name)
}
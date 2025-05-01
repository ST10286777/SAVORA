package com.example.savora.category

data class CategoryState
    (
    val categories: List<Category> = emptyList(),
    val category: String = "",
    val isAddingCategory: Boolean =false

    )

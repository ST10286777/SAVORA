package com.example.savora.category

interface CategoryEvent
{
        object SaveCategory: CategoryEvent
        data class SetCategory(val category: String) : CategoryEvent
        object ShowDialog: CategoryEvent
        object HideDialog: CategoryEvent
        data class  DeleteCategory(val category: Category): CategoryEvent

}
package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Parcelable
import com.example.simplenotes.domain.entities.Category
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FilterOptions(
    val categories: List<Category>?,
    val priority: Int?,
    val status: Boolean?
): Parcelable

package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterOptions(
    val categories: List<String>,
    val priority: Int,
    val status: Boolean
): Parcelable

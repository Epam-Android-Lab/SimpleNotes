package com.example.simplenotes.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
        var id: String = "",
        var name: String = ""
): Parcelable
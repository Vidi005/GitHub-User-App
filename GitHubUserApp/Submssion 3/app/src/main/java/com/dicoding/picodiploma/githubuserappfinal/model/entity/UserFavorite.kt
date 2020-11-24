package com.dicoding.picodiploma.githubuserappfinal.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite(
    val id: Int = 0,
    val avatar: String? = null,
    val name: String? = null,
    val username: String? = null,
    val type: String? = null,
    val company: String? = null,
    val location: String? = null
) : Parcelable
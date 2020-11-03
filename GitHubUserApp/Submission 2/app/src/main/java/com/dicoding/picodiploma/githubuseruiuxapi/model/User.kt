package com.dicoding.picodiploma.githubuseruiuxapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val avatar: String?,
                val username: String?,
                val id: Int?,
                val type: String?
) : Parcelable

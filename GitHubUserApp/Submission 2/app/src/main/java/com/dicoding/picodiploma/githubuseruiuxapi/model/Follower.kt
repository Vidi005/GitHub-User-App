package com.dicoding.picodiploma.githubuseruiuxapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Follower(var avatar: String?,
                    var username: String?,
                    var id: Int?,
                    var type: String?
) : Parcelable
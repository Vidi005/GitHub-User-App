package com.dicoding.picodiploma.githubuseruiuxapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserDetail(var name: String?,
                 var company: String?,
                 var location: String?,
                 var repositories: Int?,
                 var followers: Int?,
                 var following: Int?
) : Parcelable
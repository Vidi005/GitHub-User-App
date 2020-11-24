package com.dicoding.picodiploma.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserDetail(val name: String?,
                 val company: String?,
                 val location: String?,
                 val repositories: Int?,
                 val followers: Int?,
                 val following: Int?
) : Parcelable
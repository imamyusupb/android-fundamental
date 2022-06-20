package com.codeyuba.submisionbfaaone.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
    var avatar: Int? = 0,
    var username: String? = "",
    var name: String? = "",
    var follower: String? = "",
    var folowing: String? = "",
    var company: String? = "",
    var repository: String? = "",
    var location: String? = ""
) : Parcelable

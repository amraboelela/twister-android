//
//  User.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/09/20.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.foundation

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("u") val username: String?,
        @SerializedName("s") val rawStatus: String?,
        @SerializedName("t") val time: Long?,
        @SerializedName("pk") val privateKey: String?,
        val fullname: String?,
        val location: String?,
        val bio: String?,
        val url: String?,
        val avatar: String?,
        @SerializedName("fr") val followers: ArrayList<String>?,
        @SerializedName("fe") val followees: ArrayList<String>?,
        @SerializedName("bb") val blockedBy: ArrayList<String>?,
        @SerializedName("rb") val reportedBy: ArrayList<String>?,
) {
    constructor(username: String) : this(username, null, null,
            null, null, null, null, null, null,
            null, null, null, null)

    val key: String
        get() = username ?: ""
}

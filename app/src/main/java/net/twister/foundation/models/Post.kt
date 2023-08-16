//
//  PostRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

data class PostReference(
    @SerializedName("u") val username: String,
    @SerializedName("k") val id: String
)

data class Post(
        @SerializedName("u") val username: String,
        @SerializedName("msg") val message: String?,
        val urlContent: String?,
        @SerializedName("k") val id: String?,
        @SerializedName("t") val time: Long?,
        @SerializedName("r") val replyTo: PostReference?,
        @SerializedName("rs") val replies: ArrayList<PostReference>?,
        @SerializedName("rt") val reposted: PostReference?,
        @SerializedName("in") val isNew: Boolean?,
        @SerializedName("sig") val signature: String?,
        val lastK: Long?,
        val height: Long?,
        val translation: Hashtable<String, String>?
) {

    constructor(postKey: String) : this(username(postKey), null, null,
            null, time(postKey), null, null, null, null,
            null, null, null, null)

    val key: String
        get() = (time?.toString() ?: "0") + "-" + username

    companion object {
        fun username(postKey: String): String {
            val arr = postKey.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var result = ""
            if (arr.size > 1) {
                result = arr[1]
            }
            return result
        }

        fun time(postKey: String?): Long? {
            var result: Long? = null
            if (postKey != null) {
                val arr = postKey.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                if (arr.size > 0) {
                    result = java.lang.Long.parseLong(arr[0])
                }
            }
            return result
        }
    }
}

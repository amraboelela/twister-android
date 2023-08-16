//
//  NetworkMessage.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/31/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream
import com.google.gson.Gson
import java.io.IOException

typealias NetworkMessageCallback = (NetworkMessage, Exception?) -> Unit

enum class NetworkMessageType {
    sync, search, translateText, translatePost, getURLContent,
    createUser, newPost, login, follow, unfollow,
    getUser, updateUser, getPost,

    // Reply message types

    syncReply, searchReply, translateTextReply, translatePostReply, getURLContentReply,
    createUserReply, newPostReply, loginReply, followReply, unfollowReply,
    getUserReply, updateUserReply, getPostReply,

    unknown
}

enum class NetworkMessageStatus {
    success, failure
}

enum class NetworkMessageDirection {
    after, before
}

enum class TwisterError {
    unknownMessageType, missingData, notFound, alreadyExists, alreadySent, otherError
}

class TwisterException(error: String) : IOException(error) {
    constructor(error: TwisterError) : this(error.toString())
}

data class NetworkMessage(
        @SerializedName("mt") var rawType: String,
        @SerializedName("did") var deviceID: String = "",
        @SerializedName("t") var time: Long? = null,
        var post: Post? = null,
        @SerializedName("s") var rawStatus: String? = null,
        var posts: ArrayList<Post>? = null,
        var translation: String? = null,
        @SerializedName("e") var error: String? = null,
        var text: String? = null,
        var hashtagOrMention: String? = null,
        @SerializedName("l") var language: String? = null,
        @SerializedName("rid") var replyID: String? = null,
        @SerializedName("mu") var myUsername: String? = null,
        @SerializedName("direction") var rawDirection: String? = null,
        var user: User? = null
) {

    var type: NetworkMessageType
        get() {
            return NetworkMessageType.valueOf(rawType)
        }
        set(newValue) {
            rawType = newValue.toString()
        }

    var direction: NetworkMessageDirection
        get() {
            try {
                rawDirection?.let {
                    return NetworkMessageDirection.valueOf(it)
                } ?: return NetworkMessageDirection.after
            } catch (e: Exception)  {
                e.printStackTrace()
                return NetworkMessageDirection.after
            }
        }
        set(newValue) {
            rawDirection = newValue.toString()
        }

    internal fun encode(device: Device): ByteArrayOutputStream? {
        Gson()?.toJson(this)?.let { jsonString ->
            var result = EncryptedByteArrayOutputStream()
            result.write(jsonString.toByteArray())
            return result.encryptedWithSaltUsing(device.id)
        }
        return null
    }

    fun callbackKey(): String {
        when (this.type) {
            NetworkMessageType.sync, NetworkMessageType.syncReply -> {
                time?.let {
                    return NetworkMessageType.sync.toString() + "-" + it + "-" + direction
                } ?: post?.let {
                    return NetworkMessageType.sync.toString() + "-" + it.username + "-" + it.id
                } ?: return NetworkMessageType.sync.toString()
            }
            NetworkMessageType.getUser, NetworkMessageType.getUserReply -> {
                user?.username?.let {
                    return NetworkMessageType.getUser.toString() + "-" + it
                } ?: return NetworkMessageType.getUser.toString()
            }

            else -> {}
        }
        return ""
    }

    companion object {

        internal fun decode(data: ByteArrayOutputStream): NetworkMessage? {
            val encryptedData = EncryptedByteArrayOutputStream()
            encryptedData.write(data.toByteArray())
            //EncryptedByteArrayOutputStream.printHexEncodedString(data.toByteArray(), "decode data");
            val decryptedData = encryptedData.decryptedWithSaltUsing(MyDevice.getInstance().id)
            val decryptedString = decryptedData.toString()
            //Log.d("NetworkMessage", "decode decryptedString: " + decryptedString.substring(0, Math.min(decryptedString.length(), 300)));
            Gson()?.fromJson(decryptedString, NetworkMessage::class.java)?.let { return it }
            return null
        }
    }
}

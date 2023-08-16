//
//  UserPostRepository.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.content.Context
import com.google.gson.Gson
import android.util.Log

import java.util.ArrayList

class UserPostRepository(context: Context) : DataRepository<UserPost>(context) {
    private val userPostDao = db.userPostDao()

    fun timeFromKey(key: String?): Long? {
        var result: Long? = null
        if (key != null) {
            val arr = key.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (arr.size > 1) {
                result = java.lang.Long.parseLong(arr[1])
            }
        }
        return result
    }

    fun usernameFromKey(key: String?): String? {
        var result: String? = null
        if (key != null) {
            val arr = key.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (arr.size > 0) {
                result = arr[0]
            }
        }
        return result
    }

    fun postKeysFromUserPostKeys(keys: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (key in keys) {
            val username = usernameFromKey(key)
            val time = timeFromKey(key)
            val postKey = time.toString() + "-" + username
            result.add(postKey)
        }
        return result
    }

    override fun key(model: UserPost): String? {
        return model.key
    }

    fun save(post: Post) {
        val username = post.username
        post.time?.let { postTime ->
            val key = username + "-" + postTime
            val postKey = post.key
            save(UserPost(key, postKey))
        }
    }

    override fun save(model: UserPost) {
        key(model)?.let { key ->
            //Log.d("UserPostRepository", "save key: " + key + ", value: " + value)
            Gson().toJson(model)?.let { value ->
                val record = UserPostRecord(key, value)
                if (userPostDao.getValue(key) == null) {
                    userPostDao.insert(record)
                } else {
                    userPostDao.update(record)
                }
            }
        }
    }

    override fun modelAtKey(key: String, sendNotification: Boolean): UserPost? {
        val result = super.modelAtKey(key, sendNotification)
        if (result == null) {
            db.executor.execute {
                userPostDao.getValue(key)?.let { value ->
                    //Log.d("UserPostRepository", "modelAtKey key: " + key + ", value: " + value);
                    Gson().fromJson(value, UserPost::class.java)?.let { userPost ->
                        updateCache(key, userPost)
                        if (sendNotification) {
                            sendNotification()
                        }
                    }
                }
            }
        }
        return result
    }

    fun keysWithPrefix(prefix: String): ArrayList<String> {
        return ArrayList(userPostDao.getKeys(prefix + "%"))
    }

    companion object {

        private var instance: UserPostRepository? = null

        fun getInstance(context: Context): UserPostRepository {
            if (instance == null) {
                instance = UserPostRepository(context)
            }
            return instance!!
        }
    }
}

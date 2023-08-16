//
//  HashtagOrMentionRepository.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/19/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.*

import java.util.ArrayList
import java.util.regex.Pattern

class HashtagOrMentionRepository(context: Context) : DataRepository<HashtagOrMention>(context) {

    private val hashtagOrMentionDao = db.hashtagOrMentionDao()

    fun username(hashtagKey: String): String {
        val arr = hashtagKey.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        var result = ""
        if (arr.size > 1) {
            result = arr[1]
        }
        return result
    }

    fun hashtagFromKey(key: String?): String? {
        var result: String? = null
        if (key != null) {
            val arr = key.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (arr.size > 0) {
                result = arr[0]
            }
        }
        return result
    }

    fun postKeysFromHashtagKeys(keys: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (key in keys) {
            val hashTag = hashtagFromKey(key)
            val postKey = key.replace(hashTag + "-", "")
            result.add(postKey)
        }
        return result
    }

    fun save(post: Post) {
        post.message?.let { postMessage ->
            val hashtagPattern = Pattern.compile(HashtagOrMentionRepository.hashtagOrMentionRegex)
            val hashtagMatcher = hashtagPattern.matcher(postMessage)
            while (hashtagMatcher.find()) {
                val theMatch = hashtagMatcher.group()
                val postKey = post.key
                val key = theMatch.toLowerCase() + "-" + postKey
                save(HashtagOrMention(key, postKey))
            }
        }
    }

    override fun save(model: HashtagOrMention) {
        //Log.d("HashtagOrMentionRepository", "save key: " + key + ", value: " + value);
        model.key?.let { key ->
            Gson().toJson(model)?.let { value ->
                val record = HashtagOrMentionRecord(key, value)
                if (hashtagOrMentionDao.getValue(key) == null) {
                    hashtagOrMentionDao.insert(record)
                } else {
                    hashtagOrMentionDao.update(record)
                }
            }
        }
    }

    override fun modelAtKey(key: String, sendNotification: Boolean): HashtagOrMention? {
        val result = super.modelAtKey(key, sendNotification)
        if (result == null) {
            db.executor.execute {
                hashtagOrMentionDao.getValue(key)?.let { value ->
                    Gson().fromJson(value, HashtagOrMention::class.java)?.let { hashtagOrMention ->
                        updateCache(key, hashtagOrMention)
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
        return ArrayList(hashtagOrMentionDao.getKeys(prefix + "%"))
    }

    companion object {
        val hashtagOrMentionRegex = "[#,@][A-Za-z0-9_]+\\b"

        private var instance: HashtagOrMentionRepository? = null

        fun getInstance(context: Context): HashtagOrMentionRepository {
            if (instance == null) {
                instance = HashtagOrMentionRepository(context)
            }
            return instance!!
        }
    }
}

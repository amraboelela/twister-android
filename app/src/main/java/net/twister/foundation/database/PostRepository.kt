//
//  PostRepository.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.content.Context
import com.google.gson.Gson

class PostRepository(context: Context) : DataRepository<Post>(context) {
    private val postDao = db.postDao()

    fun biggestPostTime(): Long? {
        var result: Long = 0.toLong()
        val theCache = cache.value
        if (theCache != null) {
            try {
                for (post in theCache.values) {
                    post.time?.let {
                        if (it > result) {
                            result = it
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun smallestPostTime(): Long? {
        val lastKey = try { db.postDao().lastKey.first() } catch (e: Exception) { null }
        return Post.time(lastKey)
    }

    override fun id(model: Post): String {
        return model.id ?: ""
    }

    override fun key(model: Post): String? {
        return model.key
    }

    // This need to be called inside db.executor.execute
    override fun save(model: Post, sendNotification: Boolean): Boolean {
        var saved = false
        val key = model.key
        if (postDao.getValue(key) == null) {
            save(model)
            saved = true
            updateCache(key, model)
        }
        db.hashtagOrMentionRepository.save(model)
        db.userPostRepository.save(model)
        db.wordRepository.save(model)
        if (sendNotification) {
            sendNotification()
        }
        return saved
    }

    override fun save(model: Post) {
        val key = key(model)
        if (key != null) {
            //Log.d("PostRepository", "save key: " + key + ", value: " + value);
            Gson().toJson(model)?.let { value ->
                val record = PostRecord(key, value)
                if (postDao.getValue(key) == null) {
                    postDao.insert(record)
                } else {
                    postDao.update(record)
                }
            }
        }
    }

    override fun modelAtKey(key: String, sendNotification: Boolean): Post? {
        val result = super.modelAtKey(key, sendNotification)
        if (result == null) {
            db.executor.execute {
                postDao.getValue(key)?.let { value ->
                    Gson().fromJson(value, Post::class.java)?.let { post ->
                        updateCache(key, post)
                        if (sendNotification) {
                            sendNotification()
                        }
                    }
                }
            }
        }
        return result
    }

    companion object {
        private var instance: PostRepository? = null

        fun getInstance(context: Context): PostRepository {
            if (instance == null) {
                instance = PostRepository(context)
            }
            return instance!!
        }
    }
}

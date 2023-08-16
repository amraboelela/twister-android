//
//  PostsViewModel.kt
//  Twister
//
//  Created by Amr Aboelela on 11/2/20.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.twister

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

import net.twister.foundation.*

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class PostsViewModel(var context: Context) {
    var db: TwisterDatabase
    var serverProxy: ServerProxy

    //internal var executor: Executor
    internal var syncAfterStarted = false
    internal var syncBeforeStarted = false
    internal var userDefaults: UserDefaults

    init {
        //executor = Executors.newSingleThreadExecutor()
        db = TwisterDatabase.getDatabase(context)
        serverProxy = ServerProxy.getInstance()
        userDefaults = UserDefaults.getInstance(context)
    }

    internal fun saveImage(name: String, imageData: ByteArray) {
        val baos = ByteArrayOutputStream(imageData.size)
        baos.write(imageData, 0, imageData.size)
        try {
            val file = File(context.filesDir.path + "/" + name)
            val outputStream = FileOutputStream(file)
            baos.writeTo(outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun syncGaps(postTime: Long?, callback: () -> Unit) {
        Log.d("PostsViewModel", "syncGaps")
        serverProxy.sync(postTime, NetworkMessageDirection.before) { networkMessage, error ->
            syncAfterStarted = false
            if (error != null) {
                Log.d("PostsViewModel", "syncGaps error " + error)
            } else {
                networkMessage.posts?.let { posts ->
                    db.executor.execute {
                        var nextPostTime: Long?
                        val gapTimesStack = userDefaults.getGapTimesStack()
                        if (db.postRepository.save(posts, true) == posts.count()) { // all of them are new
                            val lastPost = posts.last()
                            nextPostTime = lastPost.time
                            if (postTime != null) { // this is an existing gap
                                gapTimesStack.remove(0)
                            }
                            gapTimesStack.put(0, nextPostTime)
                        } else { // current gap is filled
                            if (gapTimesStack.length() > 0) {
                                gapTimesStack.remove(0)
                            }
                            if (gapTimesStack.length() > 0) {
                                // get next item from the stack
                                nextPostTime = gapTimesStack.getLong(0)
                            } else {
                                if (!syncAfterStarted) {
                                    syncAfter(db.postRepository.biggestPostTime())
                                }
                                userDefaults.setGapTimesStack(gapTimesStack)
                                callback()
                                return@execute
                            }
                        }
                        userDefaults.setGapTimesStack(gapTimesStack)
                        callback()
                        syncGaps(nextPostTime) {}
                    }
                }
            }
        }
    }

    fun syncAfter(postTime: Long?) {
        //Log.d("PostsViewModel", "syncAfter")
        syncAfterStarted = true
        serverProxy.sync(postTime, NetworkMessageDirection.after) { networkMessage, error ->
            if (error != null) {
                Log.d("PostsViewModel", "syncAfter error " + error)
            } else {
                networkMessage.posts?.let { posts ->
                    db.executor.execute {
                        var waitTime: Long = 500
                        if (db.postRepository.save(posts, true) == 0) {
                            if (!syncBeforeStarted) {
                                syncBefore(null)
                            }
                            waitTime = (60 * 1000).toLong()
                        }
                        val firstPost = posts[0]
                        val nextPostTime = firstPost.time
                        Executors.newSingleThreadScheduledExecutor().schedule({
                            syncAfter(nextPostTime)
                        }, waitTime, TimeUnit.MILLISECONDS)
                    }
                }
            }
        }
    }

    fun syncBefore(postTime: Long?) {
        syncBeforeStarted = true
        var lastPostTime = postTime
        if (lastPostTime == null) {
            lastPostTime = userDefaults.getSmallestPostTime()
            if (lastPostTime == null) {
                lastPostTime = db.postRepository.smallestPostTime()
            }
        } else {
            userDefaults.setSmallestPostTime(lastPostTime)
        }
        serverProxy.sync(lastPostTime, NetworkMessageDirection.before) { networkMessage, error ->
            if (error != null) {
                Log.d("PostsViewModel", "syncBefore error " + error)
            } else {
                networkMessage.posts?.let { posts ->
                    db.executor.execute {
                        if (db.postRepository.save(posts, false) > 0) {
                            val lastPost = posts[posts.count() - 1]
                            val nextPostTime = lastPost.time
                            Executors.newSingleThreadScheduledExecutor().schedule({
                                syncBefore(nextPostTime)
                            }, 1, TimeUnit.SECONDS)
                        } else {
                            Log.d("PostsViewModel", "syncBefore done, posts.length():" + posts.count())
                        }
                    }
                }
            }
        }
    }

    fun avatarString(username: String): String? {
        //Log.d("PostsViewModel", "avatarString username: " + username)
        val user = db.userRepository.modelAtKey(username, false)
        return user?.avatar
    }

    fun avatar(username: String): Bitmap? {
        var result: Bitmap? = null
        val theAvatarString = avatarString(username)
        if (theAvatarString === "") {
            return result
        }
        if (theAvatarString != null) {
            val imageBytes = Base64.decode(theAvatarString, Base64.DEFAULT)
            result = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            saveImage(username + "-avatar.png", imageBytes)
        } else {
            var imageBytes: ByteArray? = null
            val fileName = username + "-avatar.png"
            try {
                val file = File(context.filesDir.path + "/" + fileName)
                val inputStream = FileInputStream(file)
                imageBytes = ByteArray(file.length().toInt())
                inputStream.read(imageBytes)
            } catch (e: Exception) {
                Log.d("PostsViewModel", "avatar file $fileName don't exist")
            }
            if (imageBytes != null) {
                result = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            }
        }
        if (result == null) {
            serverProxy.getUser(username) { networkMessage, error ->
                if (error != null) {
                    Log.d("PostsViewModel", "getUser error " + error)
                } else {
                    Log.d("PostsViewModel", "getUser networkMessage " + networkMessage)
                    networkMessage.user?.let { db.userRepository.save(it, true) }
                }
            }
        }
        return result
    }

    companion object {
        private var instance: PostsViewModel? = null

        fun getInstance(context: Context): PostsViewModel {
            if (instance == null) {
                instance = PostsViewModel(context)
            }
            return instance!!
        }
    }
}

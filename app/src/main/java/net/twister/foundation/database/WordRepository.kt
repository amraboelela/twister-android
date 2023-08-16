//
//  WordRepository.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 11/11/20.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.content.Context
import com.google.gson.Gson
import java.util.ArrayList

class WordRepository(context: Context) : DataRepository<Word>(context) {
    private val wordDao = db.wordDao()

    override fun key(model: Word): String? {
        return model.key
    }

    fun save(post: Post) {
        post.message?.let { postMessage ->
            for (word in Word.wordsFrom(postMessage)) {
                val username = post.username
                val time = post.time
                val postKey = "$time-$username"
                val key = word + "-" + postKey
                save(Word(key, postKey))
            }
        }
    }

    override fun save(model: Word) {
        val key = key(model)
        if (key != null) {
            db.executor.execute {
                Gson().toJson(model)?.let { value ->
                    val record = WordRecord(key, value)
                    if (wordDao.getValue(key) == null) {
                        wordDao.insert(record)
                    } else {
                        wordDao.update(record)
                    }
                }
            }
        }
    }

    override fun modelAtKey(key: String, sendNotification: Boolean): Word? {
        val result = super.modelAtKey(key, sendNotification)
        if (result == null) {
            db.executor.execute {
                wordDao.getValue(key)?.let { value ->
                    Gson().fromJson(value, Word::class.java)?.let { word ->
                        updateCache(key, word)
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
        return ArrayList(wordDao.getKeys(prefix + "%"))
    }

    fun wordFromKey(key: String?): String? {
        var result: String? = null
        if (key != null) {
            val arr = key.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (arr.size > 0) {
                result = arr[0]
            }
        }
        return result
    }

    fun postKeysFromWordKeys(keys: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (key in keys) {
            val word = wordFromKey(key)
            val postKey = key.replace(word + "-", "")
            result.add(postKey)
        }
        return result
    }

    companion object {
        private var instance: WordRepository? = null

        fun getInstance(context: Context): WordRepository {
            if (instance == null) {
                instance = WordRepository(context)
            }
            return instance!!
        }
    }

}

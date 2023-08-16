//
//  UserRepository.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.content.Context
import com.google.gson.Gson

class UserRepository(context: Context) : DataRepository<User>(context) {
    private val userDao = db.userDao()

    override fun key(model: User): String? {
        return model.key
    }

    override fun save(model: User, sendNotification: Boolean): Boolean {
        var saved = false
        val key = model.key
        if (modelAtKey(key, sendNotification) == null) {
            save(model)
            saved = true
            updateCache(key, model)
            if (sendNotification) {
                sendNotification()
            }
        }
        return saved
    }

    override fun save(model: User) {
        val key = key(model)
        if (key != null) {
            db.executor.execute {
                Gson().toJson(model)?.let { value ->
                    val record = UserRecord(key, value)
                    if (userDao.getValue(key) == null) {
                        userDao.insert(record)
                    } else {
                        userDao.update(record)
                    }
                }
            }
        }
    }

    override fun modelAtKey(key: String, sendNotification: Boolean): User? {
        val result = super.modelAtKey(key, sendNotification)
        if (result == null) {
            db.executor.execute {
                userDao.getValue(key)?.let { value ->
                    Gson().fromJson(value, User::class.java)?.let { user ->
                        updateCache(key, user)
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
        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            if (instance == null) {
                instance = UserRepository(context)
            }
            return instance!!
        }
    }

}

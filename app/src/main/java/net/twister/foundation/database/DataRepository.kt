//
//  DataRepository.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.app.ActivityManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import java.util.*

open class DataRepository<T>(val context: Context) : LifecycleOwner {

    private val mLifecycleRegistry: LifecycleRegistry

    val cache = MutableLiveData<Hashtable<String, T>>()
    internal var db: TwisterDatabase

    // Get a MemoryInfo object for the device's current memory status.
    val availableMemory: ActivityManager.MemoryInfo
        get() {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            return memoryInfo
        }

    init {
        db = TwisterDatabase.getDatabase(context)
        resetCache()
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    fun resetCache() {
        cache.setValue(Hashtable())
    }

    fun updateCache(key: String, model: T) {
        Handler(Looper.getMainLooper()).post {
            cache.value?.let { theCache ->
                theCache.put(key, model)
            }
        }
    }

    fun sendNotification() {
        Handler(Looper.getMainLooper()).post {
            cache.value?.let {
                cache.setValue(it)
            }
        }
    }

    open fun id(model: T): String {
        return ""
    }

    open fun key(model: T): String? {
        return null
    }

    fun save(models: ArrayList<T>, sendNotification: Boolean): Int {
        var saveCount = 0
        for (model in models) {
            if (save(model, false)) {
                saveCount++
            }
        }
        if (saveCount > 0) {
            if (sendNotification) {
                sendNotification()
            }
            //Log.d("DataRepository", "saved " + saveCount + " models: " + models.toString().substring(0, Math.min(models.toString().count(), 300)));
        }
        return saveCount
    }

    open fun save(model: T, sendNotification: Boolean): Boolean {
        this.key(model)?.let {
            save(model)
            updateCache(it, model)
            if (sendNotification) {
                sendNotification()
            }
        }
        return true
    }

    open fun save(model: T) {
        Log.d("DataRepository", "save model: $model")
    }

    open fun modelAtKey(key: String, sendNotification: Boolean): T? {
        return cache.value?.get(key)
    }

    companion object {
        const val pageSize = 200
    }

}

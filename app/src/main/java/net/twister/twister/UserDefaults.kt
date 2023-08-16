//
//  UserDefaults.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.content.Context
import android.content.SharedPreferences
import net.twister.twister.*
import org.json.JSONArray

class UserDefaults {

    fun reset() {
        editor?.clear()?.apply()
    }

    fun getGapTimesStack(): JSONArray {
        val gapTimesStackString = preferences?.getString(UserDefaults.gapTimesStack, "")
        try {
            return if (gapTimesStackString?.isEmpty() == true) {
                JSONArray()
            } else {
                JSONArray(gapTimesStackString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return JSONArray()
        }
    }

    fun setGapTimesStack(jsonArray: JSONArray) {
        editor?.putString(UserDefaults.gapTimesStack, jsonArray.toString())?.apply()
    }

    fun getSmallestPostTime(): Long? {
        try {
            return preferences?.getString(UserDefaults.smallestPostTime, "")?.toLong()
        } catch (e: Exception) {
            return null
        }
    }

    fun setSmallestPostTime(postTime: Long) {
        editor?.putString(UserDefaults.smallestPostTime, "" + postTime)?.apply()
    }

    companion object {
        internal val userDefaults = "userDefaults"
        internal val gapTimesStack = "gapTimesStack"
        val smallestPostTime = "smallestPostTime"

        private var instance: UserDefaults? = null
        private var preferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        fun getInstance(context: Context): UserDefaults {
            if (instance == null) {
                instance = UserDefaults()
                preferences = context.getSharedPreferences(userDefaults, Context.MODE_PRIVATE)
                editor = preferences?.edit()
            }
            return instance!!
        }
    }

}

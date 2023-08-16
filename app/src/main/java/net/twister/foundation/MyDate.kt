//
//  MyDate.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyDate(date: Long) : Date(date) {

    val isToday: Boolean
        get() {
            val formatter = SimpleDateFormat("MMM dd")
            val thisDate = formatter.format(this)
            val currentDate = formatter.format(Date())
            return if (timeDiff() < oneDay && thisDate == currentDate) {
                true
            } else false
        }

    // in seconds
    fun timeDiff(): Long {
        val currentTime = Date()
        return (currentTime.time - time) / 1000
    }

    fun time(): String {
        val formatter: SimpleDateFormat
        val locale = Locale.getDefault()
        if (timeDiff() < oneYear) {
            if (isToday) {
                formatter = SimpleDateFormat("h:mm a")
            } else {
                if (locale.toString() == "en_US") {
                    formatter = SimpleDateFormat("MMM dd, h:mm a")
                } else {
                    formatter = SimpleDateFormat("dd MMM, h:mm a")
                }
            }
        } else {
            if (locale.toString() == "en_US") {
                formatter = SimpleDateFormat("MMM dd, yyyy, h:mm a")
            } else {
                formatter = SimpleDateFormat("dd MMM yyyy, h:mm a")
            }
        }
        return formatter.format(this)
    }

    companion object {

        val oneMinute: Long = 60
        val oneHour = (60 * 60).toLong()
        val oneDay = (60 * 60 * 24).toLong()
        val oneWeek = (60 * 60 * 24 * 7).toLong()
        val oneMonth = (60 * 60 * 24 * 30).toLong()
        val oneYear = (60.0 * 60.0 * 24.0 * 365.25).toLong()

        fun now(): Long {
            return Date().time / 1000
        }

        fun minutes(seconds: Long): Long {
            return Math.ceil(seconds * 1.0 / 60.0).toLong()
        }

        fun friendlyDateStringFrom(timeInterval: Long): String {
            val now = MyDate.now()
            val diff = (now - timeInterval).toDouble()
            val formatter: SimpleDateFormat
            val locale = Locale.getDefault()
            if (diff < 0) {
                return "0s"
            } else if (diff < MyDate.oneMinute) {
                return String.format("%.0fs", diff)
            } else if (diff < MyDate.oneHour) {
                return String.format("%.0fm", diff / MyDate.oneMinute)
            } else if (diff < MyDate.oneDay) {
                return String.format("%.0fh", diff / MyDate.oneHour)
            } else if (diff < MyDate.oneWeek) {
                return String.format("%.0fd", diff / MyDate.oneDay)
            } else if (diff < MyDate.oneMonth * 6) {
                if (locale.toString() == "en_US") {
                    formatter = SimpleDateFormat("MMM dd")
                } else {
                    formatter = SimpleDateFormat("dd MMM")
                }
            } else {
                if (locale.toString() == "en_US") {
                    formatter = SimpleDateFormat("MMM dd, yy")
                } else {
                    formatter = SimpleDateFormat("dd MMM, yy")
                }
            }
            return formatter.format(Date(timeInterval * 1000))
        }
    }
}
//
//  UserPostsActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 12/27/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import androidx.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList

class UserPostsActivity : SearchActivity() {

    internal var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username = PostsAdapter.username
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        db.userRepository.modelAtKey(username, true)?.let { user ->
            title?.text = user.fullname
        } ?: run {
            title?.text = username
        }
        updateData()
    }

    override fun updateData() {
        db.executor.execute {
            val userPostKeys = db.userPostRepository.keysWithPrefix(username.toLowerCase() + "-")
            Handler(Looper.getMainLooper()).post {
                postKeys = db.userPostRepository.postKeysFromUserPostKeys(userPostKeys)
                updateUI()
            }
        }
    }

    override fun updateUI() {
        super.updateUI()
        postsAdapter.postKeys = filteredPostKeys
        postsAdapter.notifyDataSetChanged()
    }

    override fun updateWithKeys(keys: ArrayList<String>) {
        val postKeysSet = HashSet(postKeys)
        val finalResult = ArrayList<String>()
        for (key in keys) {
            if (postKeysSet.contains(key)) {
                finalResult.add(key)
            }
        }
        filteredPostKeys = ArrayList(finalResult.sortedWith(compareByDescending { it }))
        updateUI()
    }

}


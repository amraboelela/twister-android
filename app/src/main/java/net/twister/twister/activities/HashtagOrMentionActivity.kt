//
//  HashtagOrMentionActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 11/8/20.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.twister

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList

class HashtagOrMentionActivity : SearchActivity() {

    internal var hashtagOrMention = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hashtagOrMention = PostsAdapter.hashtagOrMention
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        title?.text = hashtagOrMention

        updateData()
    }

    override fun updateData() {
        db.executor.execute {
            val hashtagKeys = db.hashtagOrMentionRepository.keysWithPrefix(hashtagOrMention.toLowerCase() + "-")
            Handler(Looper.getMainLooper()).post {
                postKeys = db.hashtagOrMentionRepository.postKeysFromHashtagKeys(hashtagKeys)
                updateUI()
            }
        }
    }

    override fun updateUI() {
        super.updateUI()
        postsAdapter.postKeys = filteredPostKeys
        postsAdapter.tagText = hashtagOrMention.toLowerCase()
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


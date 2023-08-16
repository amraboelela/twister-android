//
//  PostsActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.content.ComponentCallbacks2
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList

class PostsActivity : SearchActivity(), View.OnClickListener, ComponentCallbacks2 {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title?.text = "Posts"

        postsViewModel.syncGaps(null) {
            Handler(Looper.getMainLooper()).post {
                updateData()
            }
        }
    }

    override fun updateData() {
        db.executor.execute {
            val thePostKeys = db.postDao().topKeys
            Handler(Looper.getMainLooper()).post {
                postKeys = ArrayList(thePostKeys)
                updateUI()
            }
        }
    }

    override fun updateUI() {
        super.updateUI()
        postsAdapter.postKeys = filteredPostKeys
        postsAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View) {}

    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     * @param level the memory-related event that was raised.
     */
    override fun onTrimMemory(level: Int) {

        // Determine which lifecycle or system event was raised.
        when (level) {

            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
            }

            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE, ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW, ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
            }

            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND, ComponentCallbacks2.TRIM_MEMORY_MODERATE, ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> {
            }

            else -> {
            }
        }
    }
}

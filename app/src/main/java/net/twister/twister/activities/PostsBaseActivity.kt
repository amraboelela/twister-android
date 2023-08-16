//
//  PostsBaseActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import androidx.lifecycle.Observer
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import java.util.ArrayList

open class PostsBaseActivity : ListViewActivity() {

    lateinit var postsAdapter: PostsAdapter
    lateinit var postList: ListView
    var postKeys = ArrayList<String>()
    var filteredPostKeys = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        title = findViewById(R.id.title)
        postList = findViewById(R.id.post_list)
        postsAdapter = PostsAdapter(this, R.layout.post_list)
        postList.adapter = postsAdapter
        title?.setOnClickListener {
            postList.adapter = postsAdapter
            updateData()
        }
        db.postRepository.cache.observe(this, Observer {
            updateData()
        })
        db.userRepository.cache.observe(this, Observer {
            updateData()
        })
    }

}

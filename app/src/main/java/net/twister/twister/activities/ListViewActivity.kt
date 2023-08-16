//
//  ListViewActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.widget.ListView

open class ListViewActivity : BaseActivity() {

    internal var tableView: ListView? = null

    companion object {
        internal var translateText = "Translate"
        internal var followText = "Follow"
        internal var unfollowText = "Unfollow"
        internal var followingText = "Following"
    }

}

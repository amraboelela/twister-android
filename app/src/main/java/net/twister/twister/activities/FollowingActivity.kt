//
//  FollowinActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

class FollowingActivity : ListViewActivity() {

    //ArrayList<String> users = new ArrayList();
    internal var showFollowers = false
    internal var username = ""
    internal var followersText = "Followers"

    // MARK: - Data handling

    internal fun updateTitles() {
        /*DispatchQueue.main.async {
            if self.username == "" {
                self.title = self.followingText
            } else {
                if self.showFollowers {
                    self.title = userAdapter.fullname(forUsername: self.username) + " " + self.followersText
                } else {
                    self.title = userAdapter.fullname(forUsername: self.username) + " " + self.followingText
                }
            }
        }*/
    }

    // MARK: - Delegates

    // MARK: - Actions

}


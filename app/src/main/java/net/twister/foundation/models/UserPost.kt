//
//  UserPost.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

data class UserPost(
        @Transient var key: String? = "",
        val postKey: String
)

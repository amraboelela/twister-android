//
//  HashtagRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/19/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class HashtagOrMention(
        @Transient var key: String? = "",
        val postKey: String
)

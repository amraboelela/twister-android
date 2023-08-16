//
//  HashtagOrMentionRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class HashtagOrMentionRecord(
        @PrimaryKey val key: String,
        @ColumnInfo val value: String
)

@Dao
interface HashtagOrMentionDao {

    @Query("select key from HashtagOrMentionRecord where key like :prefix ORDER BY key DESC")
    fun getKeys(prefix: String): List<String>

    @Insert
    fun insert(record: HashtagOrMentionRecord)

    @Update
    fun update(record: HashtagOrMentionRecord)

    @Query("select value from HashtagOrMentionRecord where key = :key")
    fun getValue(key: String): String?

    @Query("DELETE FROM HashtagOrMentionRecord")
    fun deleteAll()
}
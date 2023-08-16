//
//  WordRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 11/11/20.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class WordRecord(
        @PrimaryKey val key: String,
        @ColumnInfo val value: String
)

@Dao
interface WordDao {

    @Query("select key from WordRecord where key like :prefix")
    fun getKeys(prefix: String): List<String>

    @Insert
    fun insert(record: WordRecord)

    @Update
    fun update(record: WordRecord)

    @Query("select value from WordRecord where key = :key")
    fun getValue(key: String): String?

    @Query("DELETE FROM WordRecord")
    fun deleteAll()
}

//
//  UserPostRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class UserPostRecord(
        @PrimaryKey val key: String,
        @ColumnInfo val value: String
)

@Dao
interface UserPostDao {

    @Query("select key from UserPostRecord where key like :prefix ORDER BY key DESC")
    fun getKeys(prefix: String): List<String>

    @Insert
    fun insert(record: UserPostRecord)

    @Update
    fun update(record: UserPostRecord)

    @Query("select value from UserPostRecord where key = :key")
    fun getValue(key: String): String?

    @Query("DELETE FROM UserPostRecord")
    fun deleteAll()
}

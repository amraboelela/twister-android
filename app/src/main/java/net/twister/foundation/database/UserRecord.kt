//
//  UserRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 11/1/20.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class UserRecord(
        @PrimaryKey val key: String,
        @ColumnInfo val value: String
)

@Dao
interface UserDao {

    @Insert
    fun insert(record: UserRecord)

    @Update
    fun update(record: UserRecord)

    @Query("select value from UserRecord where key = :key")
    fun getValue(key: String): String?

    @Query("DELETE FROM UserRecord")
    fun deleteAll()
}

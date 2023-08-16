//
//  PostRecord.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2020 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity
data class PostRecord(
        @PrimaryKey val key: String,
        @ColumnInfo val value: String
)

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface PostDao {

    @get:Query("SELECT key from PostRecord ORDER BY key DESC LIMIT ${DataRepository.pageSize}")
    val topKeys: List<String>

    @get:Query("SELECT key from PostRecord ORDER BY key LIMIT 1")
    val lastKey: List<String>

    @Insert
    fun insert(record: PostRecord)

    @Update
    fun update(record: PostRecord)

    @Query("select value from PostRecord where key = :key")
    fun getValue(key: String): String?

    @Query("DELETE FROM PostRecord")
    fun deleteAll()
}

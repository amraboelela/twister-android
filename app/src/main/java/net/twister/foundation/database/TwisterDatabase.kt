//
//  Database.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */

@Database(entities = arrayOf(PostRecord::class,
        UserRecord::class,
        WordRecord::class,
        HashtagOrMentionRecord::class,
        UserPostRecord::class), version = 1)
abstract class TwisterDatabase : RoomDatabase() {

    internal lateinit var executor: Executor

    lateinit var postRepository: PostRepository
    lateinit var hashtagOrMentionRepository: HashtagOrMentionRepository
    lateinit var userRepository: UserRepository
    lateinit var userPostRepository: UserPostRepository
    lateinit var wordRepository: WordRepository

    internal lateinit var context: Context

    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun hashtagOrMentionDao(): HashtagOrMentionDao
    abstract fun userPostDao(): UserPostDao
    abstract fun wordDao(): WordDao

    companion object {

        private var instance: TwisterDatabase? = null

        fun getDatabase(context: Context): TwisterDatabase {
            if (instance == null) {
                synchronized(TwisterDatabase::class) {
                    if (instance == null) {

                        instance = Room.databaseBuilder(
                                context.applicationContext,
                                TwisterDatabase::class.java, "twister"
                        ).build()
                        instance?.executor = Executors.newSingleThreadExecutor()
                        instance?.context = context
                        instance?.postRepository = PostRepository.getInstance(context)
                        instance?.hashtagOrMentionRepository = HashtagOrMentionRepository.getInstance(context)
                        instance?.userRepository = UserRepository.getInstance(context)
                        instance?.userPostRepository = UserPostRepository.getInstance(context)
                        instance?.wordRepository = WordRepository.getInstance(context)
                    }
                }
            }
            return instance!!
        }

        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         */
        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                //new PopulateDbAsync(instance).execute();
            }
        }
    }

}

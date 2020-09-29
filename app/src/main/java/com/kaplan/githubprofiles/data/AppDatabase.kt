package com.kaplan.githubprofiles.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaplan.githubprofiles.ui.detail.data.DetailDao
import com.kaplan.githubprofiles.ui.detail.data.DetailItem
import com.kaplan.githubprofiles.ui.listing.data.ListingDao
import com.kaplan.githubprofiles.ui.listing.data.ListingItem

@Database(entities = [ListingItem::class, DetailItem::class],
        version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun listingDao(): ListingDao

    abstract fun detailDao(): DetailDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "github-profiles-db")
                    .build()
        }
    }
}

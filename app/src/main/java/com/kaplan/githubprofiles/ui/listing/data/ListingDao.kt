package com.kaplan.githubprofiles.ui.listing.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaplan.githubprofiles.ui.detail.data.DetailItem

@Dao
interface ListingDao {

    @Query("SELECT * FROM items ORDER BY id ASC")
    fun getListItems(): LiveData<List<ListingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listItems: List<ListingItem>)

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getPagedListItems(): DataSource.Factory<Int, ListingItem>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getListingItem(id: String): LiveData<ListingItem>

    @Query("SELECT * FROM details WHERE note != \"\"")
    fun getDetailsWithNotes() : LiveData<List<DetailItem>>
}


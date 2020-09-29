package com.kaplan.githubprofiles.ui.detail.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaplan.githubprofiles.ui.listing.data.ListingItem

@Dao
interface DetailDao {

    @Query("SELECT * FROM details WHERE login = :username")
    fun getDetailItem(username: String): LiveData<DetailItem>

    @Query("SELECT * FROM details ORDER BY id DESC")
    fun getDetailItems(): LiveData<List<DetailItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detailItem: DetailItem)
}


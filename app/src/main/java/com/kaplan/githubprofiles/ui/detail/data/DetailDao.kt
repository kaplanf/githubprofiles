package com.kaplan.githubprofiles.ui.detail.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DetailDao {

    @Query("SELECT * FROM details WHERE login = :username")
    fun getDetailItem(username: String): LiveData<DetailItem>

    @Query("SELECT * FROM details ORDER BY id DESC")
    fun getDetailItems(): LiveData<List<DetailItem>>

    @Query("SELECT * FROM details WHERE note != \"\"")
    fun getDetailsWithNotes() : List<DetailItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detailItem: DetailItem)
}


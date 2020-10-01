package com.kaplan.githubprofiles.ui.listing.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items")
data class ListingItem(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val photoUrl: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("starred_url")
    val starredUrl: String,

    @field:SerializedName("repos_url")
    val repoUrl: String,

    @field:SerializedName("type")
    val type: String,

    var hasNote: Boolean = false
    )
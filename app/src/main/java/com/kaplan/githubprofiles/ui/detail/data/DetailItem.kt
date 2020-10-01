package com.kaplan.githubprofiles.ui.detail.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "details")
data class DetailItem(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("location")
    val location: String??,

    @field:SerializedName("email")
    val email: String??,

    @field:SerializedName("bio")
    val bio: String??,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("avatar_url")
    val photoUrl: String??,

    @field:SerializedName("url")
    val url: String??,

    @field:SerializedName("starred_url")
    val starredUrl: String?,

    @field:SerializedName("repos_url")
    val repoUrl: String?,

    @field:SerializedName("blog")
    val blog: String?,

    @field:SerializedName("type")
    val type: String?,

    var note: String? = ""
)
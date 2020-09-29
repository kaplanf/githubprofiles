package com.kaplan.githubprofiles.api

import com.kaplan.githubprofiles.ui.detail.data.DetailItem
import com.kaplan.githubprofiles.ui.listing.data.ListingItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WebService {

    companion object {
        const val ENDPOINT = "https://api.github.com/"
    }

    @GET("users")
    suspend fun fetchUsers(@Query("since") since: Int? = null): Response<List<ListingItem>>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<DetailItem>
}

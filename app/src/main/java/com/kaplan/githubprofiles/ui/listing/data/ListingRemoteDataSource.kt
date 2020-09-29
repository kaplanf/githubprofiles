package com.kaplan.githubprofiles.ui.listing.data

import com.kaplan.githubprofiles.api.BaseDataSource
import com.kaplan.githubprofiles.api.WebService
import javax.inject.Inject

class ListingRemoteDataSource @Inject constructor(private val service: WebService) : BaseDataSource() {

    suspend fun fetchList(page: Int) = getResult { service.fetchUsers(page) }
}
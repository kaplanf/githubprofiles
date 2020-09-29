package com.kaplan.githubprofiles.ui.detail.data

import com.kaplan.githubprofiles.api.BaseDataSource
import com.kaplan.githubprofiles.api.WebService
import javax.inject.Inject

class DetailRemoteDataSource @Inject constructor(private val service: WebService) : BaseDataSource() {
    suspend fun fetchUser(username: String) = getResult { service.getUser(username) }
}
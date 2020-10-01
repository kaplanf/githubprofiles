package com.kaplan.githubprofiles.ui.detail.data

import androidx.lifecycle.distinctUntilChanged
import com.kaplan.githubprofiles.data.resultLiveData
import com.kaplan.githubprofiles.util.Coroutines
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val dao: DetailDao,
    private val remoteSource: DetailRemoteDataSource
) {

    fun observeUser(username: String) = resultLiveData(
        databaseQuery = { dao.getDetailItem(username) },
        networkCall = { remoteSource.fetchUser(username)},
        saveCallResult = { })
        .distinctUntilChanged()

    fun saveUser(detailItem: DetailItem)
    {
        Coroutines.io { dao.insert(detailItem) }
    }
}
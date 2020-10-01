package com.kaplan.githubprofiles.ui.detail.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.kaplan.githubprofiles.data.resultLiveData
import com.kaplan.githubprofiles.util.ConnectivityUtil
import com.kaplan.githubprofiles.util.Coroutines
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val dao: DetailDao,
    private val remoteSource: DetailRemoteDataSource, val context: Context
) {

    fun getConnection() = ConnectivityUtil.isNetworkAvailable(context)

    var isDbItemAvailable = false

    fun observeUser(username: String) = resultLiveData(
        databaseQuery = { dao.getDetailItem(username) },
        networkCall = { remoteSource.fetchUser(username) },
        saveCallResult = { saveIfNoteNull(it) })
        .distinctUntilChanged()

    fun saveUser(detailItem: DetailItem) {
        Coroutines.io { dao.insert(detailItem) }
    }

    fun saveIfNoteNull(detailItem: DetailItem) {
        if (getDetailsWithNotes().any { detailItem.id == it }.not()) {
            Coroutines.io { dao.insert(detailItem) }
        }
    }

    fun getDetailsWithNotes(): List<Int> {
        return dao.getDetailsWithNotes().map {
            it.id
        }
    }
}
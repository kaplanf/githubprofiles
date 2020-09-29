package com.kaplan.githubprofiles.ui.listing.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kaplan.githubprofiles.data.resultLiveData
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ListingRepository @Inject constructor(private val dao: ListingDao,
                                            private val listingRemoteDataSource: ListingRemoteDataSource) {

    fun observeList(page: Int) = resultLiveData(
        databaseQuery = { dao.getListItems() },
        networkCall = { listingRemoteDataSource.fetchList(page) },
        saveCallResult = { dao.insertAll(it) })
}

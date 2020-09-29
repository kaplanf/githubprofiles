package com.kaplan.githubprofiles.ui.listing.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ListingPageDataSourceFactory @Inject constructor(
    private val dataSource: ListingRemoteDataSource,
    private val dao: ListingDao,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, ListingItem>() {

    private val liveData = MutableLiveData<ListingPageDataSource>()

    override fun create(): DataSource<Int, ListingItem> {
        val source = ListingPageDataSource(dataSource, dao, scope)
        liveData.postValue(source)
        return source
    }

    companion object {
        fun pagedListConfig() = PagedList.Config.Builder()
            .setPageSize(5).setEnablePlaceholders(false)
            .build()
    }
}
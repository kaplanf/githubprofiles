package com.kaplan.githubprofiles.ui.listing.data

import androidx.paging.PageKeyedDataSource
import com.kaplan.githubprofiles.data.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ListingPageDataSource @Inject constructor(
    private val dataSource: ListingRemoteDataSource,
    private val dao: ListingDao,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, ListingItem>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ListingItem>) {
        fetchData(0, params.requestedLoadSize) {
            callback.onResult(it, null, 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ListingItem>) {
        val page = params.key
        fetchData(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ListingItem>) {
        val page = params.key
        fetchData(page, params.requestedLoadSize) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchData(page: Int, pageSize: Int, callback: (List<ListingItem>) -> Unit) {
        scope.launch(getJobErrorHandler()) {
            val response = dataSource.fetchList(page)
            if (response.status == Result.Status.SUCCESS) {
                val results = response.data!!
                dao.insertAll(results)
                callback(results)
            } else if (response.status == Result.Status.ERROR) {
                postError(response.message!!)
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.message ?: e.toString())
    }

    private fun postError(message: String) {
        Timber.e("An error happened: $message")
    }

}
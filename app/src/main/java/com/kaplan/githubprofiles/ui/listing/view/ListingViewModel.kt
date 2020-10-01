package com.kaplan.githubprofiles.ui.listing.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.kaplan.githubprofiles.data.Result
import com.kaplan.githubprofiles.di.CoroutineScropeIO
import com.kaplan.githubprofiles.ui.listing.data.ListingItem
import com.kaplan.githubprofiles.ui.listing.data.ListingRepository
import com.kaplan.githubprofiles.util.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class ListingViewModel @Inject constructor(
    val repository: ListingRepository,
    @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

    var connectivityAvailable: Boolean = false
    val pageLiveData = MutableLiveData<Int>() default 0

    var mutablelist = MutableLiveData<Result<List<ListingItem>>>()
    val list: LiveData<Result<List<ListingItem>>> get() = mutablelist
    val listMediator = MediatorLiveData<Result<List<ListingItem>>>()

    init {
        onLoadMore()
    }

    fun onLoadMore() {
//        mutablelist = repository.observeList(pageLiveData.value!!) as MutableLiveData<Result<List<ListingItem>>>
        listMediator.addSource(onLoadMore2()) {
            listMediator.postValue(it)
        }
    }

    fun onLoadMore2() =
        repository.observeList(pageLiveData.value!!) as MutableLiveData<Result<List<ListingItem>>>

    fun setPage(page: Int) {
        pageLiveData.value = page
        onLoadMore()
    }


    val dbItems by lazy { repository.observeDetailsWithNotes() }

    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}

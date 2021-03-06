package com.kaplan.githubprofiles.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.kaplan.githubprofiles.data.Result.Status.ERROR
import com.kaplan.githubprofiles.data.Result.Status.SUCCESS
import kotlinx.coroutines.Dispatchers

fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {

        var dbObjectEmpty = false

        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map {
            if(it == null)
            {
                dbObjectEmpty = true
            }
            Result.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == ERROR) {
            emit(Result.error<T>(responseStatus.message!!))
            emitSource(source)
        }
    }

fun <T> resultDbData(databaseQuery: () -> LiveData<T>): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map {
            Result.success(it)
        }
        emitSource(source)
    }

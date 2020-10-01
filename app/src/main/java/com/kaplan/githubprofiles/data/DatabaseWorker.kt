package com.kaplan.githubprofiles.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.kaplan.githubprofiles.ui.listing.data.ListingItem
import com.kaplan.githubprofiles.util.DATA_FILENAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {

            try {
                applicationContext.assets.open(DATA_FILENAME).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val type = object : TypeToken<List<ListingItem>>() {}.type
                        val list: List<ListingItem> = Gson().fromJson(jsonReader, type)

                        AppDatabase.getInstance(applicationContext).listingDao().insertAll(list)

                        Result.success()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error seeding database")
                Result.failure()
            }
        }
    }
}
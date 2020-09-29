package com.kaplan.githubprofiles.ui.detail.view

import androidx.lifecycle.ViewModel
import com.kaplan.githubprofiles.di.CoroutineScropeIO
import com.kaplan.githubprofiles.ui.detail.data.DetailRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope,
    val repository: DetailRepository
) : ViewModel() {

    lateinit var username: String

    val user by lazy {
        repository.observeUser(
            username
        )
    }
}
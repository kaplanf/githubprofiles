package com.kaplan.githubprofiles.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaplan.githubprofiles.ui.detail.view.DetailViewModel
import com.kaplan.githubprofiles.ui.listing.view.ListingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListingViewModel::class)
    abstract fun bindListingViewModel(viewModel: ListingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

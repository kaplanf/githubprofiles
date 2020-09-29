package com.kaplan.githubprofiles.di

import com.kaplan.githubprofiles.ui.detail.view.DetailFragment
import com.kaplan.githubprofiles.ui.listing.view.ListingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeListingFragment(): ListingFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
}

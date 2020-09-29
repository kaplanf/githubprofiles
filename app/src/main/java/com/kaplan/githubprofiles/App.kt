package com.kaplan.githubprofiles

import android.app.Application
import com.facebook.stetho.Stetho
import com.kaplan.githubprofiles.di.AppInjector
import com.kaplan.githubprofiles.util.CrashReportingTree
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        else Timber.plant(CrashReportingTree())

        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any>  = dispatchingAndroidInjector

}
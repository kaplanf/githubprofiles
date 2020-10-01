package com.kaplan.githubprofiles.di

import android.app.Application
import android.content.Context
import com.kaplan.githubprofiles.BuildConfig
import com.kaplan.githubprofiles.api.AuthInterceptor
import com.kaplan.githubprofiles.api.WebService
import com.kaplan.githubprofiles.data.AppDatabase
import com.kaplan.githubprofiles.ui.detail.data.DetailRemoteDataSource
import com.kaplan.githubprofiles.ui.listing.data.ListingRemoteDataSource
import com.kaplan.githubprofiles.ui.listing.view.ListingAdapter
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideWebService(@PrivateApi okhttpClient: OkHttpClient,
                           converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, WebService::class.java)

    @Singleton
    @Provides
    fun provideListingRemoteDataSource(webService: WebService)
            = ListingRemoteDataSource(webService)

    @Singleton
    @Provides
    fun provideDetailRemoteDataSource(webService: WebService) = DetailRemoteDataSource(webService)

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideListingDao(db: AppDatabase) = db.listingDao()

    @Singleton
    @Provides
    fun provideDetailDao(db: AppDatabase) = db.detailDao()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @Provides
    fun provideListAdapter(context: Context) = ListingAdapter(context)

    @PrivateApi
    @Provides
    fun providePrivateOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder()
            .addInterceptor(AuthInterceptor(BuildConfig.API_DEVELOPER_TOKEN)).build()
    }

    private fun createRetrofit(
            okhttpClient: OkHttpClient,
            converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(WebService.ENDPOINT)
                .client(okhttpClient)
                .addConverterFactory(converterFactory)
                .build()
    }

    private fun <T> provideService(okhttpClient: OkHttpClient,
            converterFactory: GsonConverterFactory, clazz: Class<T>): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}

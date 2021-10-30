package dev.decagon.godday.common.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.decagon.godday.common.data.api.ApiConstants
import dev.decagon.godday.common.data.api.SearchComicApi
import dev.decagon.godday.common.data.api.XkcdComicsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideXkcdComicsApi(builder: Retrofit.Builder): XkcdComicsApi =
        builder
            .baseUrl(ApiConstants.XKCD_BASE_URL)
            .build()
            .create(XkcdComicsApi::class.java)

    @Provides
    fun providesSearchComicApi(builder: Retrofit.Builder): SearchComicApi =
        builder
            .baseUrl(ApiConstants.SEARCH_COMICS_BASE_URL)
            .build()
            .create(SearchComicApi::class.java)

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())

    @Provides
    fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}
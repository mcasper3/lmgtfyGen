package com.wilderpereira.lmgtfygen.dagger.module

import com.wilderpereira.lmgtfygen.domain.repository.urlShortening.UrlShortenerApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {
    @Provides
    fun provideUrlShortenerApi(retrofit: Retrofit) = retrofit.create(UrlShortenerApi::class.java)
}
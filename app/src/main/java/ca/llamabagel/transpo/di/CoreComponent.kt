/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Converter
import javax.inject.Singleton

@Component(modules = [CoreModule::class])
@Singleton
interface CoreComponent {

    fun provideCallAdapterFactory(): CoroutineCallAdapterFactory
    fun provideConverterFactory(): Converter.Factory
    fun provideOkHttpClient(): OkHttpClient
}
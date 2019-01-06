package com.leunesmedia.artisthunt.injection.module

import android.content.Context
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.network.API.PostApi
import com.leunesmedia.artisthunt.network.API.UserApi
import com.leunesmedia.artisthunt.network.AuthenticationInterceptor
import com.leunesmedia.artisthunt.network.DateAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * NetworkModule Class is a Dagger Module that provides the API's, RetrofitInterface and OkHttpClient
 */
@Module
class NetworkModule {
    val API_BASE_URL = "http://projecten3studserver03.westeurope.cloudapp.azure.com:3001"

    /**
     * Provides the UserAPI with (retrofit)
     */
    @Provides
    internal fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
    /**
     * Provides the PostAPI with (retrofit)
     */
    @Provides
    internal fun providePostApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    /**
     * Provides Retrofit with (okHttpClient) and adds a DateAdapter to the MoshiConverter
     */
    @Provides
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {
        val dateadapter = Moshi.Builder()
            .add(DateAdapter())
            .build()
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(dateadapter))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    /**
     * Provides the OkHttpClient with (authToken). The client adds an AuthenticationInterceptor
     */
    @Provides
    internal fun provideOkHttpClient(authToken: String): OkHttpClient {
        if (authToken != "none") {
            val interceptor = AuthenticationInterceptor(authToken)
            return OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
            }.build()
        }

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()
    }
    /**
     * Provides the User's Bearer Token from Shared Preferences with (context)
     */
    @Provides
    internal fun getAuthToken(context: Context): String {
        return context
            .getSharedPreferences(context.getString(R.string.sharedPreferenceUserDetailsKey), Context.MODE_PRIVATE)
            .getString(context.getString(R.string.authTokenKey), "none")!!
    }

}
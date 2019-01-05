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

@Module
class NetworkModule {
    val API_BASE_URL = "http://projecten3studserver03.westeurope.cloudapp.azure.com:3001"
    val LOCAL_BASE_URL = "http://192.168.0.177:3000"

    @Provides
    internal fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    internal fun providePostApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

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

    @Provides
    internal fun getAuthToken(context: Context): String {
        return context
            .getSharedPreferences(context.getString(R.string.sharedPreferenceUserDetailsKey), Context.MODE_PRIVATE)
            .getString(context.getString(R.string.authTokenKey), "none")!!
    }

}
package com.leunesmedia.artisthunt.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor Class which adds Bearer Token to the API call
 */
class AuthenticationInterceptor(val authToken: String): Interceptor {
    /**
     * Adds bearer Token to API call
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val builder = oldRequest.newBuilder()
            .header("Authorization", "Bearer $authToken")

        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }

}
package com.example.remindy.data.remote

import com.example.remindy.data.local.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        // register/login は認証不要
        if (path.endsWith("/auth/login") || path.endsWith("/auth/register")) {
            return chain.proceed(request)
        }
        val token = runBlocking { tokenStore.currentToken() }
        val authed = if (token != null) {
            request.newBuilder().header("Authorization", "Bearer $token").build()
        } else request
        return chain.proceed(authed)
    }
}

package com.example.remindy.ui.common

import retrofit2.HttpException

sealed interface LoadState {
    data object Idle : LoadState
    data object Loading : LoadState
    data class Error(val message: String) : LoadState
}

fun debugMessage(e: Exception): String {
    if (e is HttpException) {
        val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull() ?: "(no body)"
        return "HTTP ${e.code()} ${e.message()}\nURL: ${e.response()?.raw()?.request?.url}\nBody: $body"
    }
    return buildString {
        append(e.toString())
        var cause = e.cause
        while (cause != null) {
            append("\nCaused by: $cause")
            cause = cause.cause
        }
    }
}

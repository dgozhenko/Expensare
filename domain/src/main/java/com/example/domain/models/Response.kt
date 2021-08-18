package com.example.domain.models

import android.os.AsyncTask

data class Response<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Response<T> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T?): Response<T> {
            return Response(Status.ERROR, data, message)
        }

        fun <T> loading(data: T?): Response<T> {
            return Response(Status.LOADING, data, null)
        }
    }
}

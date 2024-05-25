package com.example.cultuasturias.data

import com.example.cultuasturias.status.AppStatus

sealed class ApiResult <out T> (val status: AppStatus, val data: T?, val message:String?) {
    data class Success<out R>(val _data: R): ApiResult<R>(
        status = AppStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String): ApiResult<Nothing>(
        status = AppStatus.ERROR,
        data = null,
        message = exception
    )
    // Loading ???
}
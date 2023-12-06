package com.example.glance.ui

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository {
    const val LOADING = "LOADING"
    const val SUCCESS = "SUCCESS"
    const val ERROR = "ERROR"

    var count: Int = 0

    fun getStates(): Flow<String> {
        return flow {
            emit(LOADING)
            delay(3000)
            emit(SUCCESS)
        }
    }
}
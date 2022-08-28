package com.acc.goodwill.data.source

import kotlinx.coroutines.flow.Flow

interface Preferences {
    fun <T> set(key: String, value: T)
    fun <T> get(key: String, defaultValue: T): T
    fun <T> getFlow(key: String, defaultValue: T): Flow<T>
    fun remove(key: String)
    fun clear()
}
package com.hardiksachan.docket.core

import kotlin.coroutines.CoroutineContext

interface DispatcherProvider {
    fun provideUIContext(): CoroutineContext
    fun provideIOContext(): CoroutineContext
}
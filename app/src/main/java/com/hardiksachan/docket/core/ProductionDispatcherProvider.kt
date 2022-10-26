package com.hardiksachan.docket.core

import kotlinx.coroutines.Dispatchers

object ProductionDispatcherProvider : DispatcherProvider {
    override fun provideUIContext() = Dispatchers.Main
    override fun provideIOContext() = Dispatchers.IO
}
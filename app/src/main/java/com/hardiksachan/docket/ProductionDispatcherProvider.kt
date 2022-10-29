package com.hardiksachan.docket

import com.hardiksachan.core.DispatcherProvider
import kotlinx.coroutines.Dispatchers

object ProductionDispatcherProvider : DispatcherProvider {
    override fun provideUIContext() = Dispatchers.Main
    override fun provideIOContext() = Dispatchers.IO
}
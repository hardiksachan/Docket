package com.hardiksachan.docket

import com.hardiksachan.core.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Singleton
    @Binds
    fun bindDispatcherProvider(
        productionDispatcherProvider: ProductionDispatcherProvider
    ): DispatcherProvider

    companion object {
        @Provides
        fun provideProductionDispatcherProvider(): ProductionDispatcherProvider = ProductionDispatcherProvider
    }
}
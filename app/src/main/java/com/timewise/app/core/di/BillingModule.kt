package com.timewise.app.core.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {

    @Provides
    @Singleton
    fun providePurchasesUpdatedListener(): PurchasesUpdatedListener =
        PurchasesUpdatedListener { _, _ -> }

    @Provides
    @Singleton
    fun provideBillingClient(
        @ApplicationContext context: Context,
        listener: PurchasesUpdatedListener
    ): BillingClient = BillingClient.newBuilder(context)
        .setListener(listener)
        .enablePendingPurchases()
        .build()

    object Skus {
        const val PREMIUM_MONTHLY = "timewise_premium_monthly"
        const val PREMIUM_ANNUAL  = "timewise_premium_annual"
    }
}
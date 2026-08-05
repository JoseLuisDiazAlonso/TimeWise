package com.timewise.app.domain.repository

import android.app.Activity
import com.timewise.app.domain.model.PurchaseState
import com.timewise.app.domain.model.SubscriptionPlan
import kotlinx.coroutines.flow.Flow

/**
 * Esta interface lo que hace es aislar el resto de la app del Billing
 *
 * **/

interface BillingRepository {
    fun observeAvailablePlans () : Flow<List<SubscriptionPlan>>
    fun observePurchaseState () : Flow<PurchaseState>
    fun launchPurchaseFlow (activity: Activity, plan: SubscriptionPlan)
    suspend fun verifyExistingPurchases()
}
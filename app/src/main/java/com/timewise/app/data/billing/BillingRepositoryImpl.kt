package com.timewise.app.data.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.timewise.app.domain.model.PurchaseState
import com.timewise.app.domain.model.SubscriptionPeriod
import com.timewise.app.domain.model.SubscriptionPlan
import com.timewise.app.domain.repository.BillingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "BillingRepository"
private const val SUBSCRIPTION_PRODUCT_ID = "timewise_premium"

class BillingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BillingRepository, PurchasesUpdatedListener {

    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Verifying)
    private val purchaseStateFlow = _purchaseState.asStateFlow()

    // Caché: basePlanId -> (ProductDetails, offerToken), necesario para lanzar la compra
    private val offerCache = mutableMapOf<String, Pair<ProductDetails, String>>()

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .enablePrepaidPlans()
                .build()
        )
        .build()

    init {
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                    Log.e(TAG, "onBillingSetupFinished failed: ${result.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "Billing service disconnected, reconnecting...")
                startConnection()
            }
        })
    }

    override fun observeAvailablePlans(): Flow<List<SubscriptionPlan>> = flow {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(SUBSCRIPTION_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            ).build()

        val result = billingClient.queryProductDetails(params)

        if (result.billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(
                TAG,
                "queryProductDetails failed [${result.billingResult.responseCode}]: " +
                        result.billingResult.debugMessage
            )
            emit(emptyList())
            return@flow
        }

        val plans = result.productDetailsList.orEmpty().flatMap { details ->
            details.toSubscriptionPlans()
        }

        if (plans.isEmpty()) {
            Log.w(TAG, "productDetailsList vacío para productId=$SUBSCRIPTION_PRODUCT_ID")
        }

        emit(plans)
    }

    override fun observePurchaseState(): Flow<PurchaseState> = purchaseStateFlow

    override fun launchPurchaseFlow(activity: Activity, plan: SubscriptionPlan) {
        val (productDetails, offerToken) = offerCache[plan.basePlanId] ?: run {
            Log.e(TAG, "No hay oferta cacheada para basePlanId=${plan.basePlanId}")
            return
        }

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        billingClient.launchBillingFlow(activity, flowParams)
    }

    override suspend fun verifyExistingPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val result = billingClient.queryPurchasesAsync(params)
        if (result.billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(TAG, "queryPurchasesAsync failed: ${result.billingResult.debugMessage}")
            return
        }

        if (result.purchasesList.isEmpty()) {
            _purchaseState.value = PurchaseState.NotPurchased
        } else {
            result.purchasesList.forEach { handlePurchase(it) }
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: List<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK ->
                purchases?.forEach { handlePurchase(it) }

            BillingClient.BillingResponseCode.USER_CANCELED ->
                _purchaseState.value = PurchaseState.NotPurchased

            else -> {
                Log.e(TAG, "onPurchasesUpdated error [${result.responseCode}]: ${result.debugMessage}")
                _purchaseState.value = PurchaseState.Error(result.debugMessage)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            return
        }

        _purchaseState.value = PurchaseState.Purchased

        if (!purchase.isAcknowledged) {
            val ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(ackParams) { ackResult ->
                if (ackResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    Log.e(TAG, "acknowledgePurchase failed: ${ackResult.debugMessage}")
                }
            }
        }
    }

    private fun ProductDetails.toSubscriptionPlans(): List<SubscriptionPlan> {
        return subscriptionOfferDetails.orEmpty().mapNotNull { offer ->
            val pricingPhase = offer.pricingPhases.pricingPhaseList.firstOrNull()
                ?: return@mapNotNull null

            offerCache[offer.basePlanId] = this to offer.offerToken

            SubscriptionPlan(
                productId = productId,
                basePlanId = offer.basePlanId,
                formattedPrice = pricingPhase.formattedPrice,
                period = offer.basePlanId.toSubscriptionPeriod()
            )
        }
    }

    private fun String.toSubscriptionPeriod(): SubscriptionPeriod =
        if (contains("annual", ignoreCase = true) || contains("anual", ignoreCase = true)) {
            SubscriptionPeriod.ANNUAL
        } else {
            SubscriptionPeriod.MONTHLY
        }
}
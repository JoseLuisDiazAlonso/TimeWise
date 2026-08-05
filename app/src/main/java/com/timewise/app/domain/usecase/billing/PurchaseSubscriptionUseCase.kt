package com.timewise.app.domain.usecase.billing

import android.app.Activity
import com.timewise.app.domain.model.SubscriptionPlan
import com.timewise.app.domain.repository.BillingRepository
import javax.inject.Inject

/**
 * Esta clase es el disparador de....Quiero comprar algo....es decir se ejecuta cuando el usuario
 * quiere comprar algo.
 *
 *
 * Lo que hace esta clase con un private val billingRepositori como constructor es ejecutar
 * la función execute del billingRepository cuyas variables son un activity y el plan seleccionado.
 *
 * **/

class PurchaseSubscriptionUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    fun execute(activity: Activity, plan: SubscriptionPlan) {
        billingRepository.launchPurchaseFlow(activity, plan)
    }
}
package com.timewise.app.ui.paywall


import android.app.Activity
import androidx.lifecycle.ViewModel
import com.timewise.app.domain.model.SubscriptionPlan
import com.timewise.app.domain.repository.BillingRepository
import com.timewise.app.domain.usecase.billing.PurchaseSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.PurchaseState
import kotlinx.coroutines.flow.SharingStarted

/**
 * Lo que hace esta clase es conectar la pantalla de upgrade con los casos de uso de billing.
 * Expone los planes disponibles y estado de compra como StateFlow.
 *
 * **/

@HiltViewModel
class PaywallViewModel @Inject constructor(
    billingRepository : BillingRepository,
    private val purchaseSubscriptionUseCase: PurchaseSubscriptionUseCase
) : ViewModel() {
    val plans : StateFlow<List<SubscriptionPlan>> =
        billingRepository.observeAvailablePlans()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val purchaseState : StateFlow<PurchaseState> = billingRepository.observePurchaseState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PurchaseState.NotPurchased)

    fun onPlanSelected (activity: Activity, plan: SubscriptionPlan) {
        purchaseSubscriptionUseCase.execute(activity, plan)
    }

}
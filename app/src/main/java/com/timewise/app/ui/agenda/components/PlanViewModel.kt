package com.timewise.app.ui.components

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.timewise.app.domain.model.SubscriptionPlan
import com.timewise.app.domain.usecase.billing.PurchaseSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val purchaseSubscriptionUseCase: PurchaseSubscriptionUseCase
) : ViewModel() {

    fun onPlanSelected(activity: Activity, plan: SubscriptionPlan) {
        purchaseSubscriptionUseCase.execute(activity, plan)
    }
}
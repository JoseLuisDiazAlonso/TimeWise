package com.timewise.app.domain.model

sealed class PurchaseState {
    data object NotPurchased : PurchaseState()
    data object Pending : PurchaseState()
    data object Purchased : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}


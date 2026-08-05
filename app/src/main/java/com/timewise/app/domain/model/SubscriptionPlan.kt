package com.timewise.app.domain.model

enum class SubscriptionPeriod {MONTHLY, ANNUAL}

data class SubscriptionPlan(
    val productId: String,
    val basePlanId: String,
    val formattedPrice: String,
    val period: SubscriptionPeriod,
)

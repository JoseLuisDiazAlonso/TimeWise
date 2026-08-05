package com.timewise.app.domain.usecase.billing

import com.timewise.app.domain.repository.BillingRepository
import javax.inject.Inject

/**
 * Esta clase lo que hará es verificar si el usuario ya ha comprado algún plan.
 *
 * Se injecta un constructor con private val billingRepository. Se ejecutará una función
 * que es execute que será un suspend y ejecutará la función de billingRepository de
 * verifyExistingPurchases
 *
 * **/

class VerifyPremiumStatusUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend fun execute() {
        billingRepository.verifyExistingPurchases()
    }
}
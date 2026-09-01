package com.timewise.app.domain.ads

import com.timewise.app.domain.repository.PremiumRepository
import com.timewise.app.domain.repository.TemporaryUnlockRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsFeatureUnlockedUseCaseTest {

    private lateinit var premiumRepository: PremiumRepository
    private lateinit var temporaryUnlockRepository: TemporaryUnlockRepository
    private lateinit var useCase: IsFeatureUnlockedUseCase

    @Before
    fun setUp() {
        premiumRepository = mockk()
        temporaryUnlockRepository = mockk()
        useCase = IsFeatureUnlockedUseCase(premiumRepository, temporaryUnlockRepository)
    }

    @Test
    fun `devuelve true cuando el usuario es premium`() = runTest {
        every { premiumRepository.observeIsPremium() } returns flowOf(true)
        coEvery { temporaryUnlockRepository.isUnlockActive() } returns false

        assertTrue(useCase.execute())
    }

    @Test
    fun `devuelve true cuando hay desbloqueo temporal aunque no sea premium`() = runTest {
        every { premiumRepository.observeIsPremium() } returns flowOf(false)
        coEvery { temporaryUnlockRepository.isUnlockActive() } returns true

        assertTrue(useCase.execute())
    }

    @Test
    fun `devuelve false cuando no es premium ni hay desbloqueo temporal`() = runTest {
        every { premiumRepository.observeIsPremium() } returns flowOf(false)
        coEvery { temporaryUnlockRepository.isUnlockActive() } returns false

        assertFalse(useCase.execute())
    }

    @Test
    fun `devuelve true cuando ambas condiciones son verdaderas`() = runTest {
        every { premiumRepository.observeIsPremium() } returns flowOf(true)
        coEvery { temporaryUnlockRepository.isUnlockActive() } returns true

        assertTrue(useCase.execute())
    }
}
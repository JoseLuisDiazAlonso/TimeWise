package com.timewise.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.timewise.app.domain.model.PurchaseState
import com.timewise.app.domain.repository.BillingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PremiumRepositoryImplTest {

    private lateinit var billingRepository: BillingRepository
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: PremiumRepositoryImpl

    @Before
    fun setUp() {
        billingRepository = mockk()
        context = mockk()
        sharedPreferences = mockk(relaxed = true)
        editor = mockk(relaxed = true)

        every { context.getSharedPreferences("premium_prefs", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor

        repository = PremiumRepositoryImpl(billingRepository, context)
    }

    @Test
    fun `readPersistedFlag lee el valor guardado`() {
        every { sharedPreferences.getBoolean("key_is_premium", false) } returns true

        assertTrue(repository.readPersistedFlag())
    }

    @Test
    fun `readPersistedFlag devuelve false por defecto`() {
        every { sharedPreferences.getBoolean("key_is_premium", false) } returns false

        assertFalse(repository.readPersistedFlag())
    }

    @Test
    fun `persistPremiumFlag guarda y aplica el valor`() {
        repository.persistPremiumFlag(true)

        verify { editor.putBoolean("key_is_premium", true) }
        verify { editor.apply() }
    }

    @Test
    fun `observeIsPremium emite primero el valor persistido y luego el estado real`() = runTest {
        every { sharedPreferences.getBoolean("key_is_premium", false) } returns false
        every { billingRepository.observePurchaseState() } returns flowOf(PurchaseState.Purchased)

        val emissions = repository.observeIsPremium().toList()

        assertEquals(listOf(false, true), emissions)
    }

    @Test
    fun `observeIsPremium persiste el nuevo estado recibido del billing`() = runTest {
        every { sharedPreferences.getBoolean("key_is_premium", false) } returns false
        every { billingRepository.observePurchaseState() } returns flowOf(PurchaseState.Purchased)

        repository.observeIsPremium().toList()

        verify { editor.putBoolean("key_is_premium", true) }
    }
}
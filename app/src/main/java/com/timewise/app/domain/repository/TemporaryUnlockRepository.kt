package com.timewise.app.domain.repository

/**
 * El desbloqueo por recompensa es temporal por lo que necesita expirar solo. Estas son las
 * normas para lograr esto.
 *
 * **/

interface TemporaryUnlockRepository {
    suspend fun getUnlockExpirationTime(): Long /*Devuelve el epoch milis en que expira el desbloqueo
    o 0L si no hay ninguno activo.*/
    suspend fun grantTemporaryUnlock(durationMillis: Long) /*Otorga un desbloqueo temporal
    que expira dentro de durationMs*/
    suspend fun isUnlockActive(): Boolean /*Devuelve true si hay un desbloqueo activo,
    false si no lo hay.*/
}
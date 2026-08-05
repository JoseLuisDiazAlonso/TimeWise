package com.timewise.app.domain.repository

/**
 *
 * Esta interface se utiliza para recordar cuando fue la última vez que se mostró un anuncio
 *
 * **/

interface AdFrequencyRepository {
    suspend fun getLastShownTimestamp() : Long //Devuelve el epoch millis del último instersticial mostrado.
    suspend fun updateLastShownTimestamp(timestamp: Long) //Persiste el instante en que se acaba de mostrar un interstitial.

}
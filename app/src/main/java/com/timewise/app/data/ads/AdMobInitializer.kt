package com.timewise.app.data.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**Esta clase inicializará el AdMob de la aplicaciónp para mostrar anuncios.
 *
 * **/

class AdMobInitializer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var initialized = false
    fun initialize() {
        if (!initialized) {
            MobileAds.initialize(context) {
                initialized = true
            }
        }
    }
}
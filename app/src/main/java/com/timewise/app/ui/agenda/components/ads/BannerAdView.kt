package com.timewise.app.ui.agenda.components.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.timewise.app.core.di.BannerAdUnitId
import java.lang.reflect.Modifier

/**
 *
 * Lo que hace es envolver la vista clasíca AdView con AndroidView
 *
 * */

@Composable
fun BannerAdView (
    modifier: androidx.compose.ui.Modifier,
    @BannerAdUnitId adUnitId: String
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())

            }
        }
    )
}

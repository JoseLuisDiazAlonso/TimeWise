package com.timewise.app.core.di

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.timewise.app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY) @Qualifier annotation class BannerAdUnitId
@Retention(AnnotationRetention.BINARY) @Qualifier annotation class InterstitialAdUnitId
@Retention(AnnotationRetention.BINARY) @Qualifier annotation class RewardedAdUnitId

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideMobileAds(@ApplicationContext context: Context): Boolean {
        MobileAds.initialize(context) {}
        return true
    }

    @Provides
    fun provideAdRequest(): AdRequest = AdRequest.Builder().build()

    @BannerAdUnitId
    @Provides
    fun provideBannerAdUnitId(): String =
        if (BuildConfig.USE_TEST_ADS) TEST_BANNER else BuildConfig.ADMOB_BANNER_ID

    @InterstitialAdUnitId
    @Provides
    fun provideInterstitialAdUnitId(): String =
        if (BuildConfig.USE_TEST_ADS) TEST_INTERSTITIAL else BuildConfig.ADMOB_INTER_ID

    @RewardedAdUnitId
    @Provides
    fun provideRewardedAdUnitId(): String =
        if (BuildConfig.USE_TEST_ADS) TEST_REWARDED else BuildConfig.ADMOB_REWARDED_ID

    private const val TEST_BANNER       = "ca-app-pub-3940256099942544/6300978111"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_REWARDED     = "ca-app-pub-3940256099942544/5224354917"
}
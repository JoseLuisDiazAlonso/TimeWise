import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
   // alias(libs.plugins.google.services)
    //alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties().also { props ->
    val file = rootProject.file("local.properties")
    if (file.exists()) props.load(file.inputStream())
}

android {
    namespace  = "com.timewise.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.timewise.app"
        minSdk        = libs.versions.minSdk.get().toInt()
        targetSdk     = libs.versions.targetSdk.get().toInt()
        versionCode   = libs.versions.versionCode.get().toInt()
        versionName   = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val admobAppId = localProperties.getProperty("ADMOB_APP_ID")
            ?: "ca-app-pub-3940256099942544~3347511713"
        manifestPlaceholders["admobAppId"] = admobAppId

        buildConfigField("String",  "ADMOB_APP_ID",      "\"$admobAppId\"")
        buildConfigField("String",  "ADMOB_BANNER_ID",   "\"${localProperties.getProperty("ADMOB_BANNER_ID")  ?: "ca-app-pub-3940256099942544/6300978111"}\"")
        buildConfigField("String",  "ADMOB_INTER_ID",    "\"${localProperties.getProperty("ADMOB_INTER_ID")   ?: "ca-app-pub-3940256099942544/1033173712"}\"")
        buildConfigField("String",  "ADMOB_REWARDED_ID", "\"${localProperties.getProperty("ADMOB_REWARDED_ID")?: "ca-app-pub-3940256099942544/5224354917"}\"")
        buildConfigField("Boolean", "USE_TEST_ADS",      "true")

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable        = true
        }
        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "USE_TEST_ADS", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose     = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.multidex)          // ← duplicado eliminado
    implementation(libs.transport.api)
    implementation(libs.remote.creation.core)
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.datastore.preferences)
    implementation(libs.work.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    //val firebaseBom = platform(libs.firebase.bom)
   // implementation(firebaseBom)
   // implementation(libs.firebase.analytics)
   // implementation(libs.firebase.crashlytics)
   //implementation(libs.firebase.config)

    implementation(libs.admob)
    implementation(libs.play.billing)
    implementation(libs.splashscreen)
    implementation(libs.lottie.compose)

    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.junit.android)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)
    implementation (libs.androix.hilt.navigation.compose)
    implementation (libs.material.icons.core)

}
package com.louislu.newsappcasestudy

import android.app.Application
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.louislu.news.presentation.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@HiltAndroidApp
class NewsAppCaseStudyApplication: Application() {

    // Application-wide CoroutineScope
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        FirebaseApp.initializeApp(this)
        Timber.i("Firebase initialized")
        Firebase.analytics.logEvent("start_up", Bundle().apply { putString("where", "application") })

        // Enable Firebase Analytics Debug Mode
        if (BuildConfig.DEBUG) { //
            Firebase.analytics.setAnalyticsCollectionEnabled(true) //
            Firebase.analytics.setUserProperty("debug_mode", "true") //
            Timber.i("FirebaseAnalytics, Debug mode enabled")
        }
    }
}
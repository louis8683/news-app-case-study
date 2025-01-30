package com.louislu.core.presentation.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun logFilterSelected(filter: String) {
        val bundle = Bundle().apply { putString("filter", filter) }
        firebaseAnalytics.logEvent("filter_used", bundle)
    }

    fun logNewsClicked(title: String) {
        val bundle = Bundle().apply { putString("news_title", title) }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun logSearchButtonClicked(source: String) {
        logNavigation(source, "search")
    }

    fun logHomeButtonClicked(source: String) {
        logNavigation(source, "home")
    }

    fun logFavoriteButtonClicked(source: String) {
        logNavigation(source, "favorite")
    }

    private fun logNavigation(source: String, target: String) {
        val bundle = Bundle().apply {
            putString("source", source)
            putString("target", target)
        }
        firebaseAnalytics.logEvent("navigation", bundle)
    }

    fun logSearch(query: String) {
        val bundle = Bundle().apply { putString("query", query) }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }
}
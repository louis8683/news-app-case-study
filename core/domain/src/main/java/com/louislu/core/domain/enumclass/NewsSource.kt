package com.louislu.core.domain.enumclass

enum class NewsSource(val apiId: String) {
    ABC_NEWS("abc-news"),
    ASSOCIATED_PRESS("associated-press"),
    AXIOS("axios"),
    BLOOMBERG("bloomberg"),
    CBS_NEWS("cbs-news"),
    WALL_STREET_JOURNAL("the-wall-street-journal"),
    WASHINGTON_POST("the-washington-post");

    companion object {
        // Convert a single API ID to a NewsSource enum
        fun fromApiId(apiId: String): NewsSource? {
            return entries.find { it.apiId.equals(apiId, ignoreCase = true) }
        }

        // Convert a list of NewsSource enums into a comma-separated string for the API request
        fun toCommaSeparatedString(sources: List<NewsSource>): String {
            return sources.joinToString(separator = ",") { it.apiId }
        }

        // Hard coded for compile time usage
        const val ALL_SOURCES = "abc-news,associated-press,axios,bloomberg,cbs-news,the-wall-street-journal,the-washington-post"
    }
}
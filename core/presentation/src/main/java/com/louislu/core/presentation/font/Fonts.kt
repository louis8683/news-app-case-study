package com.louislu.core.presentation.font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.louislu.core.presentation.R

object Fonts {
    private val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    private val rokkittFont = GoogleFont("Rokkitt")

    val rokkittFontFamily = FontFamily(
        Font(googleFont = rokkittFont, fontProvider = provider)
    )
}


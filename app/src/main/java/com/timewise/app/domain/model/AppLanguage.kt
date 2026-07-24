package com.timewise.app.domain.model

/**
 * Esta clase modela los idiomas como un enum con el código de idioma ISO "es" o "en"
 * *
 * */

/**
 * Lo que hacemos es un enum (que es un tipo de dato que que representa un conjunto cerrado
 * y fijo de posibles valores*
 * */

enum class AppLanguage (val languageCode: String) {
    SPANISH("es"),
    ENGLISH("en");

    companion object {
        fun fromCode (code: String) : AppLanguage =
            entries.find { it.languageCode == code } ?: SPANISH
    }
}
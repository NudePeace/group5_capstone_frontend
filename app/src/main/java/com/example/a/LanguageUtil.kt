package com.example.a

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageUtil {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANGUAGE = "language"

    // 저장된 언어코드 가져오기 (없으면 기본값 ko)
    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "ko") ?: "ko"
    }

    // 언어코드 저장하기
    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_LANGUAGE, languageCode)
            .apply()
    }

    // 주어진 context에 언어 적용해서 새 context 리턴하기
    fun applyLanguage(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    // 이미 저장된 언어를 현재 context에 적용하기
    fun applySavedLanguage(context: Context): Context {
        val lang = getSavedLanguage(context)
        return applyLanguage(context, lang)
    }
}
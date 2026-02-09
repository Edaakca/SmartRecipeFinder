@file:Suppress("DEPRECATION")

package com.example.smartrecipefinder.Util


import java.text.Normalizer
import java.util.Locale


fun String.toSearchable(): String {
    return Normalizer.normalize(this.lowercase(Locale("tr", "TR")), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}"), "")
        .trim()
}
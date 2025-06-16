package com.upsaclay.common.domain.extensions


fun String.uppercaseFirstLetter(): String = this.replaceFirstChar { it.uppercase() }

fun String.capitalizeWordsRegex(): String {
    return this.replace(Regex("\\b\\w")) {
            it.value.uppercase()
        }
}
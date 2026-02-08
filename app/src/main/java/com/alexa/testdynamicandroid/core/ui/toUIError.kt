package com.alexa.testdynamicandroid.core.ui

/**
 * Extension to clean up SDK error strings into human-readable text.
 */
fun String?.toUIError(): String {
    if (this.isNullOrBlank()) return "An unknown error occurred"

    val messageRegex = Regex("""\"message\":\"([^\"]+)\"""")
    val messageMatch = messageRegex.find(this)
    if (messageMatch != null) {
        return messageMatch.groups[1]?.value ?: "Error occurred"
    }

    val errorRegex = Regex("""\"error\":\"([^\"]+)\"""")
    val errorMatch = errorRegex.find(this)
    if (errorMatch != null) {
        return errorMatch.groups[1]?.value ?: "Request failed"
    }

    return "Something went wrong. please, try again"
}
package com.clurio.scmu.mailbox.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDateForUser(dateStr: String?): String {
    if (dateStr == null) return "Unknown"

    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        val dateTime = LocalDateTime.parse(dateStr, inputFormatter)

        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        "Not picked up yet"
    }
}
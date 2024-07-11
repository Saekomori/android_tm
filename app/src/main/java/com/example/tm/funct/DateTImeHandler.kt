package com.example.tm.funct

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTImeHandler {
    fun parseDateTime(dateTimeString: String?): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            null
        }
    }
}
package cmp.yelpexplorer.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

interface DateTimeFormatter {
    fun formatDate(dateTime: String): String
    fun formatTime(time: String): String
}

class DateTimeFormatterImpl : DateTimeFormatter {
    override fun formatDate(dateTime: String): String {
        return dateFormatter.format(dateTimeParser.parse(dateTime).date)
    }

    override fun formatTime(time: String): String {
        return timeFormatter.format(timeParser.parse(time))
    }

    private val dateTimeParser = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
    } // or LocalDateTime.Formats.ISO

    private val dateFormatter = LocalDate.Format {
        monthNumber()
        char('/')
        day()
        char('/')
        year()
    }

    private val timeParser = LocalTime.Format {
        hour()
        minute()
    }

    private val timeFormatter = LocalTime.Format {
        amPmHour(padding = Padding.NONE)
        char(':')
        minute()
        char(' ')
        amPmMarker(am = "AM", pm = "PM")
    }
}

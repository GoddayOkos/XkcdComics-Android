package dev.decagon.godday.xkcdcomicsapp.common.domain.model

import java.text.DateFormat
import java.util.*

data class XkcdComics(
    val id: Long,
    val day: String,
    val month: String,
    val title: String,
    val year: String,
    val imageUrl: String,
    val shortDescription: String,
    private val _description: String
) {

    // Method to get the comic post date according to the device calendar format
    fun getDatePosted(): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
        val date = dateFormat.format(Calendar.getInstance().apply {
            set(year.toInt(), month.toInt() - 1, day.toInt())
        }.time)

        return date
    }

    // Formatted description without brackets
    val description: String
        get() = _description.replace("[\\[\\]\\{\\}\\(\\)]".toRegex(), "")
}
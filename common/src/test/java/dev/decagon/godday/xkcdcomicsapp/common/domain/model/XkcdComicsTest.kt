package dev.decagon.godday.xkcdcomicsapp.common.domain.model

import org.junit.Assert
import org.junit.Test
import java.text.DateFormat
import java.util.*


class XkcdComicsTest {

    private val xkcdComics1 = XkcdComics(
        id = 1,
        day = "1",
        month = "1",
        title = "Barrel - Part 1",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg",
        shortDescription = "Don't we all.",
        _description = "[[A boy sits in a barrel which is floating in an ocean.]]\\nBoy: " +
                "I wonder where I'll float next?\\n[[The barrel drifts into the distance. " +
                "Nothing else can be seen.]]\\n{{Alt: Don't we all.}}"
    )

    private val xkcdComics2 = XkcdComics(
        id = 1,
        day = "1",
        month = "1",
        title = "Barrel - Part 1",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg",
        shortDescription = "Don't we all.",
        _description = "A boy sits in a barrel which is floating in an ocean.\\nBoy: " +
                "I wonder where I'll float next?\\nThe barrel drifts into the distance. " +
                "Nothing else can be seen.\\nAlt: Don't we all."
    )

    private val xkcdComics3 = XkcdComics(
        id = 1,
        day = "1",
        month = "1",
        title = "Barrel - Part 1",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg",
        shortDescription = "Don't we all.",
        _description = "[[]]{{}}"
    )

    private val xkcdComics4 = XkcdComics(
        id = 1,
        day = "1",
        month = "1",
        title = "Barrel - Part 1",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg",
        shortDescription = "Don't we all.",
        _description = ""
    )

    /*
        I'm not hard coding the expected date value here as a String because
        the way the date string is formatted would depend on the configure of
        the device on which it is executed due to the Locale.getDefault() method.
     */
    private val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
    private val expectedDate = dateFormat.format(Calendar.getInstance().apply {
        set(2006, 0, 1)
    }.time)

    private val expectedDescription = "A boy sits in a barrel which is floating in an ocean.\\nBoy: " +
            "I wonder where I'll float next?\\nThe barrel drifts into the distance. " +
            "Nothing else can be seen.\\nAlt: Don't we all."

    @Test
    fun xkcdComics_getDatePosted_returnsFormattedDate_basedOnDeviceLocalDateFormat() {
        // Given
        val comic = xkcdComics1

        // When
        val datePosted = comic.getDatePosted()

        // Then
        Assert.assertEquals(expectedDate, datePosted)
    }

    @Test
    fun xkcdComics_descriptionWithBrackets_returnsStringWithoutBrackets() {
        // Given
        val comic = xkcdComics1

        // When
        val formattedDescription = comic.description

        // Then
        Assert.assertEquals(expectedDescription, formattedDescription)
    }

    @Test
    fun xkcdComics_descriptionWithoutBrackets_returnsStringWithoutBrackets() {
        // Given
        val comic = xkcdComics2

        // When
        val formattedDescription = comic.description

        // Then
        Assert.assertEquals(expectedDescription, formattedDescription)
    }

    @Test
    fun xkcdComics_descriptionWithOnlyBrackets_returnsEmptyString() {
        // Given
        val comic = xkcdComics3

        // When
        val formattedDescription = comic.description

        // Then
        Assert.assertEquals("", formattedDescription)
    }

    @Test
    fun xkcdComics_descriptionWithEmptyString_returnsEmptyString() {
        // Given
        val comic = xkcdComics4

        // When
        val formattedDescription = comic.description

        // Then
        Assert.assertEquals("", formattedDescription)
    }
}
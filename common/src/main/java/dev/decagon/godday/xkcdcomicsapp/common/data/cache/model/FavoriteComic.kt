package dev.decagon.godday.xkcdcomicsapp.common.data.cache.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import kotlinx.parcelize.Parcelize

/**
 * This class represent the database table used for storing/caching users'
 * favorite comics so that they will be able to view them offline.
 * This class also has methods for mapping it's instances to and from the
 * domain XkcdComic model.
 */
@Entity(tableName = "favourite_comics")
@Parcelize
data class FavoriteComic(
    @PrimaryKey
    val id: Long,
    val day: String,
    val month: String,
    val title: String,
    val year: String,
    val imageUrl: String,
    val shortDescription: String,
    val description: String
) : Parcelable {

    fun toDomain(): XkcdComics {
        val favoriteComic = this
        return XkcdComics(
            id = favoriteComic.id,
            day = favoriteComic.day,
            title = favoriteComic.title,
            month = favoriteComic.month,
            year = favoriteComic.year,
            imageUrl = favoriteComic.imageUrl,
            shortDescription = favoriteComic.shortDescription,
            _description = favoriteComic.description
        )
    }

    companion object {
        fun fromDomain(domainModel: XkcdComics): FavoriteComic {
            return FavoriteComic(
                id = domainModel.id,
                day = domainModel.day,
                month = domainModel.month,
                year = domainModel.year,
                title = domainModel.title,
                imageUrl = domainModel.imageUrl,
                shortDescription = domainModel.shortDescription,
                description = domainModel.description
            )
        }
    }
}
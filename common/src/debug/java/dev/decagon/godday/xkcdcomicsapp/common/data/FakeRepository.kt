package dev.decagon.godday.xkcdcomicsapp.common.data

import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * A fake repository class which is used in place of the actual repository
 * for testing purpose only.
 */
class FakeRepository @Inject constructor() : ComicRepository {

    val xkcdComics1 = XkcdComics(
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

    val xkcdComics2 = XkcdComics(
        id = 2,
        day = "1",
        month = "1",
        title = "Petit Trees (sketch)",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/tree_cropped_(1).jpg",
        shortDescription = "Petit' being a reference to Le Petit Prince, which " +
                "I only thought about halfway through the sketch",
        _description = "[[A boy sits in a barrel which is floating in an ocean.]]\\nBoy: " +
                "I wonder where I'll float next?\\n[[The barrel drifts into the distance. " +
                "Nothing else can be seen.]]\\n{{Alt: Don't we all.}}"
    )

    private val xkcdComics3 = XkcdComics(
        id = 3,
        day = "1",
        month = "1",
        title = "Petit",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/tree_cropped_(1).jpg",
        shortDescription = "Petit' being a reference to Le Petit Prince, which " +
                "I only thought about halfway through the sketch",
        _description = "[[A boy sits in a barrel which is floating in an ocean.]]\\nBoy: " +
                "I wonder where I'll float next?\\n[[The barrel drifts into the distance. " +
                "Nothing else can be seen.]]\\n{{Alt: Don't we all.}}"
    )

    val remoteComicList: List<XkcdComics> =
        mutableListOf(xkcdComics1, xkcdComics2, xkcdComics3)

    val favoriteComicList: MutableList<FavoriteComic> = mutableListOf()

    fun getFavoriteComicListSize(): Int = favoriteComicList.size

    override suspend fun getXkcdComicById(id: Long): XkcdComics? {
        return remoteComicList.find { it.id == id }
    }

    override suspend fun searchXkcdComic(query: String): List<XkcdComics> {
        return remoteComicList.filter { it.title.contains(query, ignoreCase = true) }
    }

    override suspend fun saveFavoriteComic(comics: XkcdComics) {
        favoriteComicList.add(FavoriteComic.fromDomain(comics))
    }

    override fun getFavouriteComics(): Flow<List<XkcdComics>> = flow { emit(remoteComicList) }
}
package dev.decagon.godday.xkcdcomicsapp.common.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteComicsDao {
    @Query("SELECT * FROM favourite_comics ORDER BY id ASC")
    fun getFavoriteComics(): Flow<List<FavoriteComic>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteComic(favoriteComic: FavoriteComic)
}
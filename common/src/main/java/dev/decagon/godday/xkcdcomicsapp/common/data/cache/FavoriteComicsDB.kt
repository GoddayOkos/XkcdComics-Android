package dev.decagon.godday.xkcdcomicsapp.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.dao.FavoriteComicsDao
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic

@Database(entities = [FavoriteComic::class], version = 1)
abstract class FavoriteComicsDB : RoomDatabase() {
    abstract fun favoriteComicsDao(): FavoriteComicsDao
}
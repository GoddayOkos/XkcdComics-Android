package dev.decagon.godday.xkcdcomicsapp.common.data.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.Cache
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.FavoriteComicsDB
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.RoomCache
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.dao.FavoriteComicsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): FavoriteComicsDB {
            return Room.databaseBuilder(
                context,
                FavoriteComicsDB::class.java,
                "favorite_comic.db")
                .build()
        }

        @Provides
        fun provideFavoriteComicsDao(favoriteComicsDB: FavoriteComicsDB): FavoriteComicsDao {
            return favoriteComicsDB.favoriteComicsDao()
        }
    }
}
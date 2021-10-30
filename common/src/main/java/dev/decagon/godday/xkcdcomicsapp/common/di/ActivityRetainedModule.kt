package dev.decagon.godday.xkcdcomicsapp.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.decagon.godday.xkcdcomicsapp.common.data.repository.XkcdComicRepository
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import dev.decagon.godday.xkcdcomicsapp.common.utils.CoroutineDispatchersProvider
import dev.decagon.godday.xkcdcomicsapp.common.utils.DispatchersProvider

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindComicRepository(repository: XkcdComicRepository): ComicRepository

    @Binds
    abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider):
            DispatchersProvider
}
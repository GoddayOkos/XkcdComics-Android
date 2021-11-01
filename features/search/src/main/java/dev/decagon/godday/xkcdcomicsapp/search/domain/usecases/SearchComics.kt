package dev.decagon.godday.xkcdcomicsapp.search.domain.usecases

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchComics @Inject constructor(private val comicRepository: ComicRepository) {
    suspend operator fun invoke(query: String): Flow<List<XkcdComics>> {
        return flow {
            emit(comicRepository.searchXkcdComic(query))
        }
    }
}
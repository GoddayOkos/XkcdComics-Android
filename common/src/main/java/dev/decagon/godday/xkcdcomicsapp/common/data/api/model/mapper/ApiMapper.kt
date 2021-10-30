package dev.decagon.godday.xkcdcomicsapp.common.data.api.model.mapper

/**
 * This is used for mapping API responses (DTO's) models into
 * domain models
 */
interface ApiMapper<E, D> {
    fun mapToDomain(apiDTO: E): D
}
package com.turingalan.pokemon.data

import com.turingalan.pokemon.data.model.Pokemon
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz abstracta que define operaciones de datos.
 * Implementada por:
 * - PokemonLocalDataSource (BD local)
 * - PokemonRemoteDataSource (API remota)
 *
 * El Repository la usa para:
 * - Obtener datos del Remote (para sincronizar)
 * - Observar datos del Local (para actualizar UI)
 */
interface PokemonDataSource {
    suspend fun readAll(): Result<List<Pokemon>>
    suspend fun readOne(id: Long): Result<Pokemon>
    fun observe(): Flow<Result<List<Pokemon>>>
}
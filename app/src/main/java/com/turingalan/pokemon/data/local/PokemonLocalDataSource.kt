package com.turingalan.pokemon.data.local

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonLocalDataSource @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonDataSource {

    override suspend fun readAll(): Result<List<Pokemon>> {
        return try {
            val entities = pokemonDao.getAll()
            Result.success(entities.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readOne(id: Long): Result<Pokemon> {
        return try {
            val entity = pokemonDao.readPokemonById(id)
            if (entity != null) {
                Result.success(entity.toModel())
            } else {
                Result.failure(Exception("Pokemon no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observe(): Flow<Result<List<Pokemon>>> {
        return pokemonDao.observeAll().map { entities ->
            Result.success(entities.toModel())
        }
    }

    /**
     * Guardar Pokémon en BD local.
     * Se usa desde Repository cuando sincroniza del Remote.
     */
    suspend fun saveAll(pokemons: List<Pokemon>) {
        val entities = pokemons.map { it.toEntity() }
        pokemonDao.insertAll(entities)
    }
}

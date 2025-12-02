package com.turingalan.pokemon.data.remote

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.remote.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRemoteDataSource @Inject constructor(
    private val api: PokemonApi
) : PokemonDataSource {

    /**
     * Obtener todos los Pokémon del API remoto.
     * ⚠️ Lento: hace N+1 requests (1 lista + 40 detalles)
     */
    override suspend fun readAll(): Result<List<Pokemon>> {
        return try {
            val response = api.readAll(limit = 40, offset = 0)

            if (response.isSuccessful) {
                val body = response.body() ?: return Result.failure(
                    Exception("Body vacío")
                )

                val finalList = mutableListOf<Pokemon>()
                for (item in body.results) {
                    try {
                        val detailResponse = api.readOneByName(item.name)
                        if (detailResponse.isSuccessful) {
                            val pokemon = detailResponse.body()?.toModel()
                            if (pokemon != null) {
                                finalList.add(pokemon)
                            }
                        }
                    } catch (e: Exception) {
                        continue
                    }
                }

                Result.success(finalList)
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun readOne(id: Long): Result<Pokemon> {
        return try {
            val response = api.readOneById(id)

            if (response.isSuccessful) {
                val pokemon = response.body()?.toModel()
                if (pokemon != null) {
                    Result.success(pokemon)
                } else {
                    Result.failure(Exception("Respuesta vacía"))
                }
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override fun observe(): Flow<Result<List<Pokemon>>> {
        return flow {
            val result = readAll()
            emit(result)
        }
    }
}

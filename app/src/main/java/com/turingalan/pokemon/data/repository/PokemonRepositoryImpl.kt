package com.turingalan.pokemon.data.repository

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.di.LocalDataSource
import com.turingalan.pokemon.di.RemoteDataSource
import com.turingalan.pokemon.data.local.PokemonLocalDataSource
import com.turingalan.pokemon.data.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    /**
     * Remote con Qualifier: obtiene del API
     */
    @RemoteDataSource private val remoteDataSource: PokemonDataSource,

    /**
     * Local con Qualifier: observa BD
     */
    @LocalDataSource private val localDataSource: PokemonDataSource,

    /**
     * Implementación local (para llamar saveAll())
     */
    private val localDataSourceImpl: PokemonLocalDataSource,

    /**
     * Scope para lanzar refresh() en background
     */
    private val scope: CoroutineScope
) : PokemonRepository {

    override suspend fun readOne(id: Long): Result<Pokemon> {
        return remoteDataSource.readOne(id)
    }

    override suspend fun readAll(): Result<List<Pokemon>> {
        return remoteDataSource.readAll()
    }

    /**
     * 🎯 MÉTODO CLAVE: Observar cambios en tiempo real.
     *
     * Flujo:
     * 1. Lanza refresh() en corrutina de background
     * 2. Retorna inmediatamente un Flow de BD Local
     * 3. Cuando refresh() termina, guarda en Local
     * 4. Local emite automáticamente
     * 5. UI recibe sin hacer nada
     */
    override fun observe(): Flow<Result<List<Pokemon>>> {
        // Lanzar refresco en background (NO bloqueante)
        scope.launch {
            refresh()
        }

        // Retornar observable de BD local INMEDIATAMENTE
        return localDataSource.observe()
    }

    /**
     * Sincronizar: obtener del Remote y guardar en Local.
     */
    private suspend fun refresh() {
        val resultRemote = remoteDataSource.readAll()

        if (resultRemote.isSuccess) {
            val pokemonList = resultRemote.getOrNull()
            if (pokemonList != null) {
                // Guardar en BD Local
                localDataSourceImpl.saveAll(pokemonList)
                // DAO emite automáticamente aquí ✨
            }
        }
    }
}

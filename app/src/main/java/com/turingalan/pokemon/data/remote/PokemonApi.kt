package com.turingalan.pokemon.data.remote

import com.turingalan.pokemon.data.remote.model.PokemonListRemote
import com.turingalan.pokemon.data.remote.model.PokemonRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    /**
     * GET https://pokeapi.co/api/v2/pokemon?limit=40&offset=0
     */
    @GET("/api/v2/pokemon")
    suspend fun readAll(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonListRemote>

    /**
     * GET https://pokeapi.co/api/v2/pokemon/1
     */
    @GET("/api/v2/pokemon/{id}")
    suspend fun readOneById(@Path("id") id: Long): Response<PokemonRemote>

    /**
     * GET https://pokeapi.co/api/v2/pokemon/bulbasaur
     */
    @GET("/api/v2/pokemon/{name}")
    suspend fun readOneByName(@Path("name") name: String): Response<PokemonRemote>
}

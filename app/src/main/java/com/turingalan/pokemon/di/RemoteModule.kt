package com.turingalan.pokemon.di

import com.turingalan.pokemon.data.remote.PokemonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo que provisiona instancias de Retrofit y CoroutineScope.
 * @Provides porque hay lógica de construcción.
 */
@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    /**
     * Provisionar Retrofit (cliente HTTP).
     * Se usa como base para crear PokemonApi.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provisionar PokemonApi (endpoints).
     * Depende de Retrofit que Hilt proporciona automáticamente.
     */
    @Provides
    @Singleton
    fun providePokemonApi(retrofit: Retrofit): PokemonApi {
        return retrofit.create(PokemonApi::class.java)
    }

    /**
     * Provisionar CoroutineScope global.
     * SupervisorJob() → si una corrutina falla, las otras continúan.
     */
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}

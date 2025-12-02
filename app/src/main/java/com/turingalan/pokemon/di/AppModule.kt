package com.turingalan.pokemon.di

import com.turingalan.pokemon.data.PokemonDataSource
import com.turingalan.pokemon.data.local.PokemonLocalDataSource
import com.turingalan.pokemon.data.remote.PokemonRemoteDataSource
import com.turingalan.pokemon.data.repository.PokemonRepository
import com.turingalan.pokemon.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Módulo abstracto que provee bindings para interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Cuando alguien pida @RemoteDataSource PokemonDataSource,
     * Hilt inyecta PokemonRemoteDataSource
     */
    @Singleton
    @Binds
    @RemoteDataSource
    abstract fun bindsRemotePokemonDataSource(
        ds: PokemonRemoteDataSource
    ): PokemonDataSource

    /**
     * Cuando alguien pida @LocalDataSource PokemonDataSource,
     * Hilt inyecta PokemonLocalDataSource
     */
    @Singleton
    @Binds
    @LocalDataSource
    abstract fun bindsLocalPokemonDataSource(
        ds: PokemonLocalDataSource
    ): PokemonDataSource

    /**
     * Cuando alguien pida PokemonRepository,
     * Hilt inyecta PokemonRepositoryImpl
     */
    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        repository: PokemonRepositoryImpl
    ): PokemonRepository
}

/**
 * Qualifier personalizado: marca implementación REMOTA
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource

/**
 * Qualifier personalizado: marca implementación LOCAL
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource

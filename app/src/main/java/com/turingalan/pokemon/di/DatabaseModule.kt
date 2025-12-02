package com.turingalan.pokemon.di

import android.content.Context
import androidx.room.Room
import com.turingalan.pokemon.data.local.PokemonDao
import com.turingalan.pokemon.data.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo que provisiona instancias de Room.
 * @Provides porque hay lógica de construcción.
 * @Singleton porque solo queremos UNA instancia en toda la app.
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = PokemonDatabase::class.java,
            name = "pokemon-db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(
        database: PokemonDatabase
    ): PokemonDao {
        return database.getPokemonDao()
    }
}

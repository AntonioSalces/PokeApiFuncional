package com.turingalan.pokemon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Delete
    suspend fun delete(pokemon: PokemonEntity): Int

    @Query("SELECT * FROM pokemon")
    suspend fun getAll(): List<PokemonEntity>

    /**
     * 🎯 OBSERVAR TODOS (REACTIVO).
     * Flow<T> emite cada vez que la tabla cambia.
     * Sin polling, sin timers, completamente reactivo.
     */
    @Query("SELECT * FROM pokemon")
    fun observeAll(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun readPokemonById(id: Long): PokemonEntity?
}

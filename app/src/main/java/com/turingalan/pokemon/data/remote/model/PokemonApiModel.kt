package com.turingalan.pokemon.data.remote.model

import com.google.gson.annotations.SerializedName
import com.turingalan.pokemon.data.model.Pokemon

data class PokemonListRemote(
    val results: List<PokemonListItemRemote>
)

data class PokemonListItemRemote(
    val name: String,
    val url: String
)

data class PokemonRemote(
    val id: Long,
    val name: String,
    val sprites: PokemonSprites,
)

data class PokemonSprites(
    val front_default: String,
    val other: PokemonOtherSprite
)

data class PokemonOtherSprite(
    @SerializedName("official-artwork")
    val officialArtwork: PokemonOfficialArtwork
)

data class PokemonOfficialArtwork(
    val front_default: String
)

/**
 * Convertir DTO del API → Modelo de dominio
 */
fun PokemonRemote.toModel(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        sprite = this.sprites.front_default,
        artwork = this.sprites.other.officialArtwork.front_default
    )
}

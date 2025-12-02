package com.turingalan.pokemon.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.turingalan.pokemon.data.model.Pokemon
import com.turingalan.pokemon.data.repository.PokemonRepository
import com.turingalan.pokemon.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val artwork: String? = null,
    val error: String? = null
)

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailUiState> =
        MutableStateFlow(DetailUiState(isLoading = true))

    val uiState: StateFlow<DetailUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                // Obtener ID de la ruta
                val route = savedStateHandle.toRoute<Route.Detail>()
                val pokemonId = route.id

                // Llamar readOne() que retorna Result<Pokemon>
                val result = pokemonRepository.readOne(pokemonId)

                result.onSuccess { pokemon ->
                    // Success: actualizar estado
                    _uiState.value = pokemon.toDetailUiState()
                }.onFailure { exception ->
                    // Error: mostrar mensaje
                    _uiState.value = DetailUiState(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState(
                    isLoading = false,
                    error = e.message ?: "Error al obtener Pokémon"
                )
            }
        }
    }
}

fun Pokemon.toDetailUiState(): DetailUiState = DetailUiState(
    isLoading = false,
    name = this.name,
    artwork = this.artwork,
)

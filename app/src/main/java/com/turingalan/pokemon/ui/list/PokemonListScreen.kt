package com.turingalan.pokemon.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel(),
    onShowDetail: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is ListUiState.Initial -> {
            // Pantalla en blanco mientras carga
        }
        is ListUiState.Loading -> {
            PokemonLoadingScreen(modifier)
        }
        is ListUiState.Success -> {
            PokemonList(modifier, uiState as ListUiState.Success, onShowDetail)
        }
        is ListUiState.Error -> {
            PokemonErrorScreen(modifier, (uiState as ListUiState.Error).message)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PokemonLoadingScreen(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingIndicator(
            modifier = Modifier.size(128.dp)
        )
    }
}

@Composable
private fun PokemonErrorScreen(modifier: Modifier, message: String) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun PokemonList(
    modifier: Modifier,
    uiState: ListUiState.Success,
    onShowDetail: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = uiState.pokemons,
            key = { item -> item.id }
        ) { pokemon ->
            PokemonListItemCard(
                pokemonId = pokemon.id,
                name = pokemon.name,
                sprite = pokemon.sprite,
                onShowDetail = onShowDetail
            )
        }
    }
}

@Composable
fun PokemonListItemCard(
    modifier: Modifier = Modifier,
    pokemonId: Long,
    name: String,
    sprite: String,
    onShowDetail: (Long) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .clickable(
                enabled = true,
                onClick = { onShowDetail(pokemonId) }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AsyncImage(
                model = sprite,
                contentDescription = name,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

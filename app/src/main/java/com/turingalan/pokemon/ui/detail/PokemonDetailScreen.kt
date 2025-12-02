package com.turingalan.pokemon.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PokemonDetailContent(
        modifier = modifier,
        state = uiState
    )
}

@Composable
private fun PokemonDetailContent(
    modifier: Modifier = Modifier,
    state: DetailUiState,
) {
    when {
        state.isLoading -> {
            LoadingContent(modifier)
        }
        state.error != null -> {
            ErrorContent(modifier, state.error)
        }
        else -> {
            SuccessContent(
                modifier = modifier,
                name = state.name,
                artwork = state.artwork
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
private fun ErrorContent(modifier: Modifier, error: String) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $error",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SuccessContent(
    modifier: Modifier = Modifier,
    name: String,
    artwork: String?,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!artwork.isNullOrEmpty()) {
            AsyncImage(
                model = artwork,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(300.dp)
            )
        }

        Text(
            text = name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Preview
@Composable
fun PokemonDetailScreenPreview() {
    Surface {
        PokemonDetailContent(
            state = DetailUiState(
                isLoading = false,
                name = "eevee",
                artwork = ""
            )
        )
    }
}

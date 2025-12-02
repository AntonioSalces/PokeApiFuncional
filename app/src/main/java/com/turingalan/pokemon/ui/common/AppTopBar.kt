package com.turingalan.pokemon.ui.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.turingalan.pokemon.R
import com.turingalan.pokemon.ui.navigation.Route
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    backStackEntry: NavBackStackEntry? = null,
) {
    // Obtener el título según la ruta actual
    val title = when (backStackEntry?.destination?.route) {
        "pokemon_list" -> stringResource(R.string.app_name)
        else -> {
            // Si contiene pokemon_detail, extraer el ID
            if (backStackEntry?.destination?.route?.contains("pokemon_detail") == true) {
                "Detalles"
            } else {
                stringResource(R.string.app_name)
            }
        }
    }

    TopAppBar(
        title = {
            Text(text = title)
        }
    )
}

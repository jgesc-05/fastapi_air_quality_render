package me.juanescobar.airqualityapp

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirQualityApp() {
    var selectedScreen by remember { mutableStateOf("Inicio") }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("AQI Prediction", fontWeight = FontWeight.Bold)},
                navigationIcon = {Icon(imageVector = Icons.Filled.Place, contentDescription = null)},
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Inicio") }, onClick = {
                            selectedScreen = "Inicio"
                            menuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Mapa") }, onClick = {
                            selectedScreen = "Mapa"
                            menuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Predicción") }, onClick = {
                            selectedScreen = "Predicción"
                            menuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Tipo de persona") }, onClick = {
                            selectedScreen = "Sensibilidad"
                            menuExpanded = false
                        })
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                "Inicio" -> StartScreen()
                "Mapa" -> MapScreen()
                "Predicción" -> PredictionsScreen()
                "Sensibilidad" -> TipsScreen()
            }
        }
    }
}

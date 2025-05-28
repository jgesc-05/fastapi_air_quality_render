package me.juanescobar.airqualityapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TipsScreen() {
    val spacing = 12.dp
    val scrollState = rememberScrollState()


    var hasRespiratoryOrCardioDisease by remember { mutableStateOf(false) }
    var exercisesOutdoors by remember { mutableStateOf(false) }
    var worksOutdoors by remember { mutableStateOf(false) }
    var isChildOrElderly by remember { mutableStateOf(false) }
    var isPregnantOrImmunocompromised by remember { mutableStateOf(false) }
    var hasRespiratoryAllergies by remember { mutableStateOf(false) }
    var livesNearPollution by remember { mutableStateOf(false) }

    val sensitivity = when {
        listOf(
            hasRespiratoryOrCardioDisease,
            isChildOrElderly,
            isPregnantOrImmunocompromised
        ).count { it } >= 2 -> "Eres una persona altamente sensible. Tenlo en cuenta en las predicciones"
        listOf(
            exercisesOutdoors,
            worksOutdoors,
            hasRespiratoryAllergies,
            livesNearPollution
        ).any { it } -> "Eres una persona moderadamente sensible. Tenlo en cuenta en las predicciones"
        else -> "Tu sensibilidad es baja. Tenlo en cuenta en las predicciones"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Selecciona todas las que apliquen",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            CustomCheckbox("¿Tienes enfermedades respiratorias o cardiovasculares?", hasRespiratoryOrCardioDisease) {
                hasRespiratoryOrCardioDisease = it
            }

            CustomCheckbox("¿Haces ejercicio frecuentemente al aire libre?", exercisesOutdoors) {
                exercisesOutdoors = it
            }

            CustomCheckbox("¿Trabajas o estudias al aire libre al menos 4 horas al día?", worksOutdoors) {
                worksOutdoors = it
            }

            CustomCheckbox("¿Tienes menos de 5 años o más de 65 años?", isChildOrElderly) {
                isChildOrElderly = it
            }

            CustomCheckbox("¿Estás embarazada o inmunodeprimido/a?", isPregnantOrImmunocompromised) {
                isPregnantOrImmunocompromised = it
            }

            CustomCheckbox("¿Sufres de alergias respiratorias estacionales?", hasRespiratoryAllergies) {
                hasRespiratoryAllergies = it
            }

            CustomCheckbox("¿Vives cerca de avenidas principales, fábricas o zonas contaminadas?", livesNearPollution) {
                livesNearPollution = it
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            color = when (sensitivity) {
                "Eres una persona altamente sensible. Tenlo en cuenta en las predicciones" -> Color(0xFFFFCDD2) // rojo claro
                "Eres una persona moderadamente sensible. Tenlo en cuenta en las predicciones" -> Color(0xFFFFF3CD) // amarillo claro
                else -> Color(0xFFC8E6C9) // verde claro
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Resultado: $sensitivity",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CustomCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            fontSize = 15.sp
        )
    }
}

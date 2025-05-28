package me.juanescobar.airqualityapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

@Composable
fun PredictionsScreen() {
    var sliderHour by remember { mutableFloatStateOf(0f) }
    var sliderPM25 by remember { mutableFloatStateOf(0f) }
    var sliderPM10 by remember { mutableFloatStateOf(0f) }

    var resultText by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Predicciones de AQI PM2.5",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        AQISlider(
            title = "Hora del día",
            value = sliderHour,
            valueRange = 0f..23f,
            steps = 23,
            valueLabel = "${sliderHour.toInt()}h"
        ) { sliderHour = it }

        AQISlider(
            title = "Concentración PM2.5",
            value = sliderPM25,
            valueRange = 0f..500f,
            valueLabel = "${sliderPM25.toInt()} µg/m³"
        ) { sliderPM25 = it }

        AQISlider(
            title = "Concentración PM10",
            value = sliderPM10,
            valueRange = 0f..500f,
            valueLabel = "${sliderPM10.toInt()} µg/m³"
        ) { sliderPM10 = it }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val request = AirQualityRequest(
                    hour = sliderHour.toInt(),
                    pm25 = sliderPM25,
                    pm10 = sliderPM10
                )

                api.predictAirQuality(request).enqueue(object : Callback<AirQualityResponse> {
                    override fun onResponse(
                        call: Call<AirQualityResponse>,
                        response: Response<AirQualityResponse>
                    ) {
                        resultText = if (response.isSuccessful) {
                            val result = response.body()
                            "${result?.predicted_category} ${result?.emoji}\n${result?.advice}"
                        } else {
                            "Error en la predicción: ${response.code()}"
                        }
                    }

                    override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                        resultText = "Fallo de red: ${t.message}"
                    }
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Predecir", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultText.isNotEmpty()) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color(0xFFE3F2FD)
                ),
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = resultText,
                        fontSize = 17.sp,
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AQISlider(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    valueLabel: String,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth()
        )
        Text(valueLabel, fontSize = 15.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

data class AirQualityRequest(
    val hour: Int,
    val pm25: Float,
    val pm10: Float
)

data class AirQualityResponse(
    val predicted_category: String,
    val emoji: String,
    val advice: String
)

interface AirQualityApi {
    @POST("predict/")
    fun predictAirQuality(@Body request: AirQualityRequest): Call<AirQualityResponse>
}


val retrofit = Retrofit.Builder()
    .baseUrl("https://fastapi-air-quality-render.onrender.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: AirQualityApi = retrofit.create(AirQualityApi::class.java)






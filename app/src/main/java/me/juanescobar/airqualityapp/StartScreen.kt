package me.juanescobar.airqualityapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.getValue


@Composable
fun StartScreen(viewModel: AirQualityViewModel = viewModel()) {
    val aqi = viewModel.aqi

    val (aqiText, color) = when {
        aqi == null -> "Cargando..." to Color.Gray
        aqi <= 50 -> "Bueno" to Color(0xFF43A047)
        aqi <= 100 -> "Moderado" to Color(0xFFFB8C00)
        aqi <= 150 -> "No saludable para sensibles" to Color(0xFFF4511E)
        aqi <= 200 -> "No saludable" to Color(0xFFD32F2F)
        aqi <= 300 -> "No muy saludable" to Color(0xFF7B1FA2)
        else -> "Peligroso" to Color(0xFF4E342E)
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F1F1)) // fondo suave
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido a AQI Prediction!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Conoce si es recomendable hacer ejercicio al aire libre según la calidad del aire y la hora del día, con esta app.",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Divider(thickness = 1.dp, color = Color.LightGray)

            Text(
                text = "Estado actual del aire en Bucaramanga",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )

            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (aqi != null) {
                        Text(
                            text = "AQI: $aqi",
                            color = color,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = aqiText,
                            color = color,
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic
                        )
                    } else {
                        Text(
                            text = "Cargando datos...",
                            color = Color.Gray,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            viewModel.lastUpdate?.let { isoDate ->
                val formattedDate = formatFecha(isoDate)
                Text(
                    text = "Última actualización: $formattedDate",
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }



data class AirQualityResponseS(
    val hourly: HourlyData
)

data class HourlyData(
    @SerializedName("european_aqi") val europeanAqi: List<Int>,
    val time: List<String>
)

interface OpenMeteoApi {
    @GET("v1/air-quality")
    suspend fun getAirQuality(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "european_aqi",
        @Query("timezone") timezone: String = "auto"
    ): AirQualityResponseS
}



object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://air-quality-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: OpenMeteoApi by lazy {
        retrofit.create(OpenMeteoApi::class.java)
    }

}

class AirQualityViewModel : ViewModel() {
    var aqi by mutableStateOf<Int?>(null)
        private set

    var lastUpdate by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getAirQuality(
                    latitude = 7.1193,
                    longitude = -73.1227
                )
                aqi = response.hourly.europeanAqi.firstOrNull()
                lastUpdate = response.hourly.time.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



fun formatFecha(fechaIso: String): String {
    return try {
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val outputFormat = DateTimeFormatter.ofPattern("d 'de' MMMM, hh:mm a", Locale("es"))
        val fecha = LocalDateTime.parse(fechaIso, inputFormat)
        fecha.format(outputFormat)
    } catch (e: Exception) {
        fechaIso
    }
}





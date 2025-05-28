package me.juanescobar.airqualityapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polygon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val activity = context as Activity

    var locationPermissionGranted by remember { mutableStateOf(false) }
    var showLegend by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }

    // Animaciones
    val slideAnimation by animateFloatAsState(
        targetValue = if (locationPermissionGranted) 0f else 50f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "slide"
    )

    val legendAnimation by animateFloatAsState(
        targetValue = if (showLegend) 1f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "legend"
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> locationPermissionGranted = granted }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            locationPermissionGranted = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con gradiente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            androidx.compose.ui.graphics.Color(0xFF1A1A2E),
                            androidx.compose.ui.graphics.Color(0xFF16213E)
                        )
                    )
                )
        )

        if (locationPermissionGranted) {
            // Mapa
            AndroidView(
                factory = {
                    Configuration.getInstance().load(
                        context,
                        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                    )

                    createMapView(context) { location ->
                        selectedLocation = location
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = slideAnimation.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )

            // Header flotante
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset(y = (-slideAnimation).dp)
                    .zIndex(10f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Mapa de Calidad del Aire",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color(0xFF1A1A2E)
                        )
                        Text(
                            text = "PM2.5 - Bucaramanga",
                            fontSize = 14.sp,
                            color = androidx.compose.ui.graphics.Color(0xFF666666)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón de leyenda
                        FloatingActionButton(
                            onClick = { showLegend = !showLegend },
                            modifier = Modifier.size(48.dp),
                            containerColor = androidx.compose.ui.graphics.Color(0xFF2196F3),
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Mostrar leyenda",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Botón de actualizar
                        FloatingActionButton(
                            onClick = { /* Actualizar datos */ },
                            modifier = Modifier.size(48.dp),
                            containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Leyenda animada
            AnimatedVisibility(
                visible = showLegend,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200)
                ) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .zIndex(10f)
            ) {
                LegendCard(onClose = { showLegend = false })
            }

            // Card de información de ubicación seleccionada
            selectedLocation?.let { location ->
                LocationInfoCard(
                    location = location,
                    onClose = { selectedLocation = null },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .zIndex(10f)
                )
            }

        } else {
            // Pantalla de permisos
            PermissionScreen(
                onRequestPermission = {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            )
        }
    }
}

@Composable
fun LegendCard(onClose: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Leyenda AQI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color(0xFF1A1A2E)
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = androidx.compose.ui.graphics.Color(0xFF666666)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val legendItems = listOf(
                LegendItem("0-50", "Excelente", androidx.compose.ui.graphics.Color(0xFF4CAF50)),
                LegendItem("51-100", "Moderado", androidx.compose.ui.graphics.Color(0xFFFF9800)),
                LegendItem("101-150", "Poco saludable\npara sensibles", androidx.compose.ui.graphics.Color(0xFFFF5722)),
                LegendItem("151-200", "Poco saludable", androidx.compose.ui.graphics.Color(0xFFE91E63)),
                LegendItem("201-300", "Muy poco saludable", androidx.compose.ui.graphics.Color(0xFF9C27B0)),
                LegendItem("300+", "Peligroso", androidx.compose.ui.graphics.Color(0xFF795548))
            )

            legendItems.forEach { item ->
                LegendRow(item = item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun LegendRow(item: LegendItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = item.color,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = item.color.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )

        Text(
            text = item.range,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = androidx.compose.ui.graphics.Color(0xFF333333),
            modifier = Modifier.width(60.dp)
        )

        Text(
            text = item.description,
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color(0xFF666666),
            lineHeight = 16.sp
        )
    }
}

@Composable
fun LocationInfoCard(
    location: LocationData,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(250.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = location.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color(0xFF1A1A2E)
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = androidx.compose.ui.graphics.Color(0xFF666666),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "AQI:",
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color(0xFF666666)
                )

                Text(
                    text = "${location.aqi}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = location.color
                )

                Text(
                    text = location.status,
                    fontSize = 14.sp,
                    color = location.color,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Última actualización: ${location.lastUpdate}",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color(0xFF888888)
            )
        }
    }
}

@Composable
fun PermissionScreen(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Permisos de Ubicación",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Necesitamos acceso a tu ubicación para mostrar los datos de calidad del aire en tu zona.",
            fontSize = 16.sp,
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRequestPermission,
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Conceder Permisos",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Función mejorada para crear el mapa
fun createMapView(context: Context, onLocationClick: (LocationData) -> Unit): MapView {
    return MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)

        val bucaramanga = GeoPoint(7.119349, -73.122741)
        controller.setZoom(12.0)
        controller.setCenter(bucaramanga)

        // Datos de ejemplo para diferentes ubicaciones
        val locations = listOf(
            LocationData("Centro de Bucaramanga", 38, "Excelente", "12:30 PM", bucaramanga),
        )

        locations.forEach { location ->
            val marker = Marker(this).apply {
                position = location.geoPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "${location.name}: AQI ${location.aqi}"
                setOnMarkerClickListener { _, _ ->
                    onLocationClick(location)
                    true
                }
            }

            overlays.add(marker)
            overlays.add(createAqiCircle(location.geoPoint, location.aqi))
        }
    }
}

// Función mejorada para crear el círculo de AQI
fun createAqiCircle(center: GeoPoint, aqi: Int): Overlay {
    val circle = Polygon().apply {
        val radiusInMeters = when {
            aqi <= 50 -> 1500.0
            aqi <= 100 -> 2000.0
            aqi <= 150 -> 2500.0
            aqi <= 200 -> 3000.0
            aqi <= 300 -> 3500.0
            else -> 4000.0
        }

        val (fillColor, strokeColor) = when {
            aqi <= 50 -> Pair(
                Color.argb(80, 76, 175, 80),
                Color.argb(255, 76, 175, 80)
            )
            aqi <= 100 -> Pair(
                Color.argb(80, 255, 152, 0),
                Color.argb(255, 255, 152, 0)
            )
            aqi <= 150 -> Pair(
                Color.argb(80, 255, 87, 34),
                Color.argb(255, 255, 87, 34)
            )
            aqi <= 200 -> Pair(
                Color.argb(80, 233, 30, 99),
                Color.argb(255, 233, 30, 99)
            )
            aqi <= 300 -> Pair(
                Color.argb(80, 156, 39, 176),
                Color.argb(255, 156, 39, 176)
            )
            else -> Pair(
                Color.argb(80, 121, 85, 72),
                Color.argb(255, 121, 85, 72)
            )
        }

        points = Polygon.pointsAsCircle(center, radiusInMeters)
        fillPaint.color = fillColor
        fillPaint.style = Paint.Style.FILL
        outlinePaint.color = strokeColor
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = 8f
    }
    return circle
}

// Clases de datos
data class LocationData(
    val name: String,
    val aqi: Int,
    val status: String,
    val lastUpdate: String,
    val geoPoint: GeoPoint
) {
    val color: androidx.compose.ui.graphics.Color
        get() = when {
            aqi <= 50 -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
            aqi <= 100 -> androidx.compose.ui.graphics.Color(0xFFFF9800)
            aqi <= 150 -> androidx.compose.ui.graphics.Color(0xFFFF5722)
            aqi <= 200 -> androidx.compose.ui.graphics.Color(0xFFE91E63)
            aqi <= 300 -> androidx.compose.ui.graphics.Color(0xFF9C27B0)
            else -> androidx.compose.ui.graphics.Color(0xFF795548)
        }
}

data class LegendItem(
    val range: String,
    val description: String,
    val color: androidx.compose.ui.graphics.Color
)
package com.kabi.composecomponents.internet_speed

// ============================================================
// NetworkSpeedMonitor.kt
// Drop this file anywhere in your project — fully self-contained
// Usage: Add <NetworkSpeedOverlay /> anywhere in your Compose tree
// ============================================================

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ============================================================
// DATA MODEL
// ============================================================

data class NetworkSpeedState(
    val downloadSpeed: Long = 0L,      // bytes/sec
    val uploadSpeed: Long = 0L,        // bytes/sec
    val totalDownloaded: Long = 0L,    // bytes since app start
    val totalUploaded: Long = 0L,      // bytes since app start
    val networkType: String = "Unknown",
    val isConnected: Boolean = false,
    val sessionDownload: Long = 0L,    // bytes this session
    val sessionUpload: Long = 0L       // bytes this session
)

// ============================================================
// SPEED FORMATTER
// ============================================================

fun formatSpeed(bytesPerSec: Long): String {
    return when {
        bytesPerSec >= 1_000_000 -> String.format("%.1f MB/s", bytesPerSec / 1_000_000.0)
        bytesPerSec >= 1_000 -> String.format("%.1f KB/s", bytesPerSec / 1_000.0)
        else -> "$bytesPerSec B/s"
    }
}

fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1_073_741_824 -> String.format("%.2f GB", bytes / 1_073_741_824.0)
        bytes >= 1_048_576 -> String.format("%.1f MB", bytes / 1_048_576.0)
        bytes >= 1_024 -> String.format("%.1f KB", bytes / 1_024.0)
        else -> "$bytes B"
    }
}

// ============================================================
// NETWORK TYPE DETECTOR
// ============================================================

fun getNetworkType(context: Context): Pair<String, Boolean> {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return Pair("No Network", false)
    val caps = cm.getNetworkCapabilities(network) ?: return Pair("No Network", false)

    val type = when {
        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile Data"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
        else -> "Unknown"
    }
    return Pair(type, true)
}

// ============================================================
// MAIN SPEED MONITOR —ViewModel-free, state-only
// ============================================================

@Composable
fun rememberNetworkSpeedState(
    intervalMs: Long = 1000L   // sample every 1 second
): NetworkSpeedState {
    val context = LocalContext.current
    var state by remember { mutableStateOf(NetworkSpeedState()) }

    // Track bytes at last sample
    var lastRxBytes by remember { mutableLongStateOf(TrafficStats.getTotalRxBytes()) }
    var lastTxBytes by remember { mutableLongStateOf(TrafficStats.getTotalTxBytes()) }

    // Session baseline (set once at composition)
    val sessionRxBaseline = remember { TrafficStats.getTotalRxBytes() }
    val sessionTxBaseline = remember { TrafficStats.getTotalTxBytes() }

    LaunchedEffect(Unit) {
        while (true) {
            delay(intervalMs)

            val currentRx = TrafficStats.getTotalRxBytes()
            val currentTx = TrafficStats.getTotalTxBytes()

            val downloadSpeed = if (currentRx > lastRxBytes) currentRx - lastRxBytes else 0L
            val uploadSpeed = if (currentTx > lastTxBytes) currentTx - lastTxBytes else 0L

            lastRxBytes = currentRx
            lastTxBytes = currentTx

            val (netType, isConnected) = getNetworkType(context)

            state = NetworkSpeedState(
                downloadSpeed = downloadSpeed,
                uploadSpeed = uploadSpeed,
                totalDownloaded = currentRx,
                totalUploaded = currentTx,
                networkType = netType,
                isConnected = isConnected,
                sessionDownload = if (currentRx > sessionRxBaseline) currentRx - sessionRxBaseline else 0L,
                sessionUpload = if (currentTx > sessionTxBaseline) currentTx - sessionTxBaseline else 0L
            )
        }
    }

    return state
}

// ============================================================
// COMPACT SPEED PILL — tiny overlay (top of screen)
// ============================================================

@Composable
fun NetworkSpeedPill(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val speed = rememberNetworkSpeedState()

    val indicatorColor = when {
        !speed.isConnected -> Color(0xFFEF5350)
        speed.downloadSpeed > 500_000 -> Color(0xFF66BB6A)   // > 500 KB/s green
        speed.downloadSpeed > 100_000 -> Color(0xFFFFCA28)   // > 100 KB/s yellow
        else -> Color(0xFFEF5350)      // slow red
    }

    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xDD1A1A2E)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Live indicator dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(indicatorColor)
            )

            // Download speed
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Download",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = formatSpeed(speed.downloadSpeed),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Upload speed
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Upload",
                    tint = Color(0xFFA5D6A7),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = formatSpeed(speed.uploadSpeed),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Network type badge
            Text(
                text = speed.networkType,
                color = Color(0xFFB0BEC5),
                fontSize = 10.sp
            )
        }
    }
}

// ============================================================
// EXPANDED DETAIL CARD
// ============================================================

@Composable
fun NetworkSpeedDetailCard(
    modifier: Modifier = Modifier
) {
    val speed = rememberNetworkSpeedState()

    // Animated progress for visual speed gauge (cap at 10 MB/s = 100%)
    val downloadProgress by animateFloatAsState(
        targetValue = (speed.downloadSpeed / 10_000_000f).coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = "download_progress"
    )
    val uploadProgress by animateFloatAsState(
        targetValue = (speed.uploadSpeed / 10_000_000f).coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = "upload_progress"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Network Monitor",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (speed.isConnected) Color(0xFF66BB6A)
                                else Color(0xFFEF5350)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = speed.networkType,
                        color = Color(0xFFB0BEC5),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Download row
            SpeedRow(
                label = "Download",
                speed = formatSpeed(speed.downloadSpeed),
                progress = downloadProgress,
                barColor = Color(0xFF64B5F6),
                icon = Icons.Default.KeyboardArrowDown,
                iconTint = Color(0xFF64B5F6)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Upload row
            SpeedRow(
                label = "Upload",
                speed = formatSpeed(speed.uploadSpeed),
                progress = uploadProgress,
                barColor = Color(0xFFA5D6A7),
                icon = Icons.Default.KeyboardArrowUp,
                iconTint = Color(0xFFA5D6A7)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Session usage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SessionStat(
                    label = "Session ↓",
                    value = formatBytes(speed.sessionDownload),
                    color = Color(0xFF64B5F6)
                )
                SessionStat(
                    label = "Session ↑",
                    value = formatBytes(speed.sessionUpload),
                    color = Color(0xFFA5D6A7)
                )
                SessionStat(
                    label = "Total Used",
                    value = formatBytes(speed.sessionDownload + speed.sessionUpload),
                    color = Color(0xFFFFCA28)
                )
            }
        }
    }
}

@Composable
private fun SpeedRow(
    label: String,
    speed: String,
    progress: Float,
    barColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    color = Color(0xFFB0BEC5),
                    fontSize = 12.sp
                )
            }
            Text(
                text = speed,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = barColor,
            trackColor = Color(0xFF2A2A4A),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun SessionStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color(0xFF78909C),
            fontSize = 10.sp
        )
    }
}

// FLOATING OVERLAY — add to any screen's Box layout
// Shows pill by default, expands on tap

@Composable
fun NetworkSpeedOverlay(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = !expanded,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            NetworkSpeedPill(
                modifier = Modifier.wrapContentSize(),
                onClick = { expanded = true }
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            NetworkSpeedDetailCard(
                modifier = Modifier
                    .width(300.dp)
                    .clickable { expanded = false }
            )
        }
    }
}
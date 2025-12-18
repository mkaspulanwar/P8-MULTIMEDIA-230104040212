package id.antasari.p8_multimedia_230104040212.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.antasari.p8_multimedia_230104040212.R
import id.antasari.p8_multimedia_230104040212.ui.Screen
// Import warna baru
import id.antasari.p8_multimedia_230104040212.ui.theme.BrandDarkBlue
import id.antasari.p8_multimedia_230104040212.ui.theme.BrandLightBlue
import id.antasari.p8_multimedia_230104040212.ui.theme.BrandBackground

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    // GRADIENT HEADER (Pakai warna Logo)
    val gradient = Brush.verticalGradient(
        listOf(BrandLightBlue, BrandDarkBlue) // #1f66ff ke #1800ad
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground) // Background biru muda
            .verticalScroll(scrollState)
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Sedikit lebih tinggi
                .background(gradient),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(54.dp))
                // LOGO IMAGE
                Image(
                    painter = painterResource(id = R.drawable.multimedia1),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White), // Kasih background putih biar logo menonjol
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Multimedia Studio",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Created by Anwar The Great",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // --- HERO CARD ---
        HeroCard(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .height(180.dp)
                .offset(y = (-30).dp)
        )

        // --- MENU GRID ---
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .offset(y = (-10).dp) // Geser grid sedikit ke atas
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MenuCard(
                    icon = Icons.Default.Mic,
                    title = "Record Audio",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Screen.AudioRecorder.route) }
                )
                MenuCard(
                    icon = Icons.Default.PlayArrow,
                    title = "Play Audio",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("audio_player/dummy_path") }
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MenuCard(
                    icon = Icons.Default.Videocam,
                    title = "Play Video",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("video_player/dummy_path") }
                )
                MenuCard(
                    icon = Icons.Default.CameraAlt,
                    title = "Camera & Gallery",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Screen.CameraGallery.route) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        FooterInfo()
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun HeroCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.multimedia2),
            contentDescription = "Multimedia Hero",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MenuCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon Background Lingkaran Biru Pudar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BrandLightBlue.copy(alpha = 0.1f)), // Biru Transparan
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = BrandLightBlue, // Icon warna Biru Terang
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C29)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FooterInfo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Copyright ©2025 Multimedia Studio", style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray))
        Text("M. Kaspul Anwar (230104040212)", style = MaterialTheme.typography.labelSmall.copy(color = BrandLightBlue))
    }
}
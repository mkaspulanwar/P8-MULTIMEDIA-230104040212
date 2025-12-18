package id.antasari.p8_multimedia_230104040212.ui.player

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import id.antasari.p8_multimedia_230104040212.ui.recorder.FileCard
import id.antasari.p8_multimedia_230104040212.ui.recorder.formatDuration
import id.antasari.p8_multimedia_230104040212.ui.recorder.loadAudioFiles
import id.antasari.p8_multimedia_230104040212.util.FileManagerUtility
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(
    onBack: () -> Unit,
    audioPath: String
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State untuk Player
    var currentFile by remember { mutableStateOf(File(audioPath)) }
    var audioFiles by remember { mutableStateOf(loadAudioFiles(context)) }

    // State Rename/Delete (jika ingin fitur ini juga ada di player)
    var showRenameDialog by remember { mutableStateOf<File?>(null) }
    var newFileName by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    // Init ExoPlayer
    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    var isPlaying by remember { mutableStateOf(true) }
    var position by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }

    // Efek saat file berubah -> putar lagu
    LaunchedEffect(currentFile) {
        player.stop()
        player.clearMediaItems()
        if (currentFile.exists()) {
            player.setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile)))
            player.prepare()
            player.playWhenReady = true
            isPlaying = true
        }
    }

    // Update Slider tiap 200ms
    LaunchedEffect(isPlaying) {
        while (true) {
            if (player.isPlaying) {
                position = player.currentPosition
                duration = player.duration.coerceAtLeast(1L)
                isPlaying = true
            } else {
                isPlaying = false
            }
            delay(200)
        }
    }

    // Bersihkan player saat keluar screen
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Player") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))

            // Judul File Sedang Diputar
            Text(
                text = currentFile.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // TOMBOL PLAY/PAUSE BESAR
            FloatingActionButton(
                onClick = {
                    if (isPlaying) {
                        player.pause()
                        isPlaying = false
                    } else {
                        player.play()
                        isPlaying = true
                    }
                },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // SLIDER POSISI
            Slider(
                value = position.toFloat(),
                onValueChange = {
                    position = it.toLong()
                    player.seekTo(position)
                },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatDuration(position))
                Text(formatDuration(duration))
            }

            Spacer(Modifier.height(20.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            Text("Daftar Lainnya", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(12.dp))

            // LIST FILE
            audioFiles.forEach { item ->
                FileCard(
                    data = item,
                    onPlay = { currentFile = item.file }, // Ganti lagu
                    onEdit = {
                        showRenameDialog = item.file
                        newFileName = item.file.nameWithoutExtension
                    },
                    onDelete = {
                        showDeleteDialog = item.file
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.height(30.dp))
        }
    }

    // --- DIALOG RENAME & DELETE (Sama seperti Recorder, copy logicnya) ---
    // (Jika ingin singkat, kamu bisa bikin komponen dialog terpisah,
    // tapi untuk sekarang copy-paste logika dialog dari Recorder ke sini juga oke
    // agar fitur rename/delete bekerja di layar Player juga)

    if (showRenameDialog != null) {
        val file = showRenameDialog!!
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Edit Nama File") },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("Nama baru") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.renameFile(file, "$newFileName.mp4")
                    audioFiles = loadAudioFiles(context)
                    // Jika file yang direname adalah file yang sedang diputar, update currentFile
                    if (file.absolutePath == currentFile.absolutePath) {
                        currentFile = File(currentFile.parent, "$newFileName.mp4")
                    }
                    showRenameDialog = null
                }) { Text("Simpan") }
            },
            dismissButton = { TextButton(onClick = { showRenameDialog = null }) { Text("Batal") } }
        )
    }

    if (showDeleteDialog != null) {
        val file = showDeleteDialog!!
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Hapus File?") },
            text = { Text(file.name) },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.deleteFile(file)
                    audioFiles = loadAudioFiles(context)
                    // Jika file yang diputar dihapus, stop player
                    if (file.absolutePath == currentFile.absolutePath) {
                        player.stop()
                        player.clearMediaItems()
                        isPlaying = false
                    }
                    showDeleteDialog = null
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Batal") } }
        )
    }
}
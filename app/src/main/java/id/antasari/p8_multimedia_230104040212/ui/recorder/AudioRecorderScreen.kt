package id.antasari.p8_multimedia_230104040212.ui.recorder

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import id.antasari.p8_multimedia_230104040212.ui.Screen
import id.antasari.p8_multimedia_230104040212.ui.theme.BrandLightBlue // Import Warna Biru
import id.antasari.p8_multimedia_230104040212.util.AudioFileData
import id.antasari.p8_multimedia_230104040212.util.FileManagerUtility
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioRecorderScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val scrollState = rememberScrollState()

    var isRecording by remember { mutableStateOf(false) }
    var recorder: MediaRecorder? by remember { mutableStateOf(null) }
    var outputFile by remember { mutableStateOf("") }
    var audioFiles by remember { mutableStateOf(loadAudioFiles(context)) }

    // Dialog States
    var showRenameDialog by remember { mutableStateOf<File?>(null) }
    var newFileName by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) {
        if (activity != null && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
        }
    }

    fun startRecording() {
        val fileName = "audio_${System.currentTimeMillis()}.mp4"
        val file = File(context.filesDir, fileName)
        outputFile = file.absolutePath

        val newRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        try {
            newRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)
                prepare()
                start()
            }
            recorder = newRecorder
            isRecording = true
        } catch (e: Exception) {
            Log.e("Recorder", "Start error: ${e.message}")
            isRecording = false
        }
    }

    fun stopRecording() {
        try {
            if (isRecording) recorder?.stop()
        } catch (e: Exception) {
            Log.e("Recorder", "Stop error: ${e.message}")
        } finally {
            recorder?.release()
            recorder = null
            isRecording = false
            val file = File(outputFile)
            if (file.exists() && file.length() > 0) {
                audioFiles = loadAudioFiles(context)
                val encodedPath = Uri.encode(outputFile)
                onNavigate(Screen.AudioPlayer.passPath(encodedPath))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Recorder") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // --- ANIMASI PULSING MIC ---
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (isRecording) 1.25f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse"
            )

            Box(contentAlignment = Alignment.Center) {
                // Lingkaran luar (efek denyut saat merekam)
                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.2f))
                    )
                }

                // Tombol Utama (Update Warna Biru)
                FloatingActionButton(
                    onClick = {
                        if (!isRecording) startRecording() else stopRecording()
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(100.dp),
                    // Jika merekam = MERAH, Jika tidak = BIRU TERANG (BrandLightBlue)
                    containerColor = if (isRecording) Color.Red else BrandLightBlue
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = if (isRecording) "Recording in progress..." else "Tap mic to start",
                color = if (isRecording) Color.Red else Color.Gray,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(30.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text(
                "Daftar Rekaman",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            audioFiles.forEach { item ->
                FileCard(
                    data = item,
                    onPlay = {
                        val encoded = Uri.encode(item.file.absolutePath)
                        onNavigate(Screen.AudioPlayer.passPath(encoded))
                    },
                    onEdit = { showRenameDialog = item.file; newFileName = item.file.nameWithoutExtension },
                    onDelete = { showDeleteDialog = item.file }
                )
                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.height(40.dp))
        }
    }

    // Dialogs
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
            text = { Text("Yakin hapus ${file.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.deleteFile(file)
                    audioFiles = loadAudioFiles(context)
                    showDeleteDialog = null
                }) { Text("Hapus", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Batal") } }
        )
    }
}

fun loadAudioFiles(context: Context): List<AudioFileData> {
    return FileManagerUtility.getAllAudioFiles(context).map { file ->
        AudioFileData(
            file = file,
            durationMs = FileManagerUtility.getAudioDuration(context, file),
            sizeText = FileManagerUtility.formatFileSize(file.length())
        )
    }
}

fun formatDuration(ms: Long): String {
    val sec = ms / 1000
    val min = sec / 60
    val s = sec % 60
    return "%02d:%02d".format(min, s)
}

// --- UPDATED FILE CARD (BLUE THEME) ---
@Composable
fun FileCard(
    data: AudioFileData,
    onPlay: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Box Icon (Background Biru Pudar)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrandLightBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AudioFile,
                    contentDescription = null,
                    tint = BrandLightBlue // Icon Biru
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.file.name,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${formatDuration(data.durationMs)} • ${data.sizeText}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Rename", tint = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252))
            }
        }
    }
}
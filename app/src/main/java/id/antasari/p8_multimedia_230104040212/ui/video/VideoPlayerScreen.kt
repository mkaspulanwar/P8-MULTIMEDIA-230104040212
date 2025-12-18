package id.antasari.p8_multimedia_230104040212.ui.video

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import id.antasari.p8_multimedia_230104040212.util.FileManagerUtility
import id.antasari.p8_multimedia_230104040212.util.VideoFileData
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    onBack: () -> Unit,
    videoPath: String
) {
    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()

    var currentFile by remember { mutableStateOf(File(videoPath)) }
    var videoFiles by remember { mutableStateOf(loadVideoFiles(context)) }
    var isFullscreen by remember { mutableStateOf(false) }

    var isPlaying by remember { mutableStateOf(true) }
    var position by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var showRenameDialog by remember { mutableStateOf<File?>(null) }
    var newFileName by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            if (currentFile.exists()) {
                setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile)))
                prepare()
                play()
            }
        }
    }

    LaunchedEffect(currentFile) {
        player.stop()
        if (currentFile.exists()) {
            player.setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile)))
            player.prepare()
            player.play()
            isPlaying = true
            scale = 1f; offsetX = 0f; offsetY = 0f
        }
    }

    LaunchedEffect(isPlaying) {
        while (true) {
            if (player.isPlaying) {
                position = player.currentPosition
                duration = player.duration.coerceAtLeast(1L)
            }
            delay(200)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                player.pause()
                isPlaying = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    fun toggleFullscreen() {
        isFullscreen = !isFullscreen
        activity.requestedOrientation = if (isFullscreen) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    Scaffold(
        topBar = {
            if (!isFullscreen) {
                TopAppBar(
                    title = { Text("Video Player") },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                    actions = { IconButton(onClick = { toggleFullscreen() }) { Icon(Icons.Default.Fullscreen, "Fullscreen") } }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isFullscreen) PaddingValues(0.dp) else padding)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isFullscreen) 320.dp else 240.dp)
                    .background(Color.Black)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 4f)
                            val maxOffset = (scale - 1) * 300
                            offsetX = (offsetX + pan.x).coerceIn(-maxOffset, maxOffset)
                            offsetY = (offsetY + pan.y).coerceIn(-maxOffset, maxOffset)
                        }
                    }
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            this.useController = false
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (!isFullscreen) {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = currentFile.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = {
                        if (player.isPlaying) { player.pause(); isPlaying = false } else { player.play(); isPlaying = true }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null)
                }

                Spacer(Modifier.height(16.dp))

                Slider(
                    value = position.toFloat(),
                    onValueChange = { position = it.toLong(); player.seekTo(position) },
                    valueRange = 0f..duration.toFloat(),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatDuration(position))
                    Text(formatDuration(duration))
                }

                Spacer(Modifier.height(24.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                Text(
                    "Daftar Video",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(Modifier.height(12.dp))

                videoFiles.forEach { item ->
                    VideoFileCard(
                        data = item,
                        onPlay = { currentFile = item.file },
                        onEdit = { showRenameDialog = item.file; newFileName = item.file.nameWithoutExtension },
                        onDelete = { showDeleteDialog = item.file }
                    )
                    Spacer(Modifier.height(12.dp))
                }
                Spacer(Modifier.height(40.dp))
            } else {
                IconButton(
                    onClick = { toggleFullscreen() },
                    modifier = Modifier.align(Alignment.End).padding(16.dp)
                ) {
                    Icon(Icons.Default.FullscreenExit, "Exit Fullscreen", tint = Color.White)
                }
            }
        }
    }

    // Dialog Rename & Delete logic same as before (omitted for brevity, copied from AudioRecorder logic if needed)
    if (showRenameDialog != null) {
        val file = showRenameDialog!!
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Edit Nama Video") },
            text = { OutlinedTextField(value = newFileName, onValueChange = { newFileName = it }) },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.renameFile(file, "$newFileName.mp4")
                    videoFiles = loadVideoFiles(context)
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
            title = { Text("Hapus Video?") },
            text = { Text(file.name) },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.deleteFile(file)
                    videoFiles = loadVideoFiles(context)
                    if (file.absolutePath == currentFile.absolutePath) {
                        player.stop(); isPlaying = false
                    }
                    showDeleteDialog = null
                }) { Text("Ya", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Batal") } }
        )
    }
}

fun loadVideoFiles(context: android.content.Context): List<VideoFileData> {
    return FileManagerUtility.getAllVideoFiles(context).map { file ->
        VideoFileData(
            file = file,
            durationMs = FileManagerUtility.getVideoDuration(file),
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

// --- NEW VIDEO FILE CARD UI (MATCHING AUDIO STYLE) ---
@Composable
fun VideoFileCard(
    data: VideoFileData,
    onPlay: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD)), // Biru muda untuk video
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.VideoFile,
                    contentDescription = null,
                    tint = Color(0xFF1976D2) // Biru
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
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            }
        }
    }
}
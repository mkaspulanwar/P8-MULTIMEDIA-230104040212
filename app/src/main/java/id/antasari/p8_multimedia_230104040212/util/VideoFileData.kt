package id.antasari.p8_multimedia_230104040212.util

import java.io.File

data class VideoFileData(
    val file: File,
    val durationMs: Long,
    val sizeText: String
)
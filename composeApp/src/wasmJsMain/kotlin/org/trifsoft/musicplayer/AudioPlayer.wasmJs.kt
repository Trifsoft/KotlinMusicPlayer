package org.trifsoft.musicplayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement
import org.w3c.dom.url.URL.Companion.createObjectURL

@OptIn(ExperimentalWasmJsInterop::class)
class WasmJsAudioPlayer(file: KmpFile): AudioPlayer {
    private val audioElement: HTMLAudioElement = document.createElement("audio") as HTMLAudioElement
    private var isPlaying = false
    private val durationState = mutableStateOf(0f)

    init {
        // Convert KmpFile to Blob and create object URL
        val blob = file.file.slice()
        val url = createObjectURL(blob)
        audioElement.src = url
        audioElement.onloadedmetadata = {
            durationState.value = audioElement.duration.toFloat()
        }

        // Preload metadata to get duration
        audioElement.preload = "metadata"
    }

    override val duration: Float by durationState
    override val title: String? = null

    override fun seekTo(value: Float) {
        audioElement.currentTime = value.toDouble()
    }

    override fun playPause() {
        if (isPlaying) {
            audioElement.pause()
            isPlaying = false
        } else {
            audioElement.play()
            isPlaying = true
        }
    }
}

actual fun getAudioPlayer(file: KmpFile): AudioPlayer = WasmJsAudioPlayer(file)
package org.trifsoft.musicplayer

import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalForeignApi::class)
class IOSAudioPlayer(file: KmpFile): AudioPlayer {
    val avAudioPlayer = AVAudioPlayer(file.url, null)
    init {
        avAudioPlayer.prepareToPlay()
    }
    override val duration: Float
        get() = avAudioPlayer.duration.seconds.inWholeSeconds.toFloat()

    override fun seekTo(value: Float) {
        avAudioPlayer.setCurrentTime(value.toDouble())
    }

    override fun playPause() {
        if(avAudioPlayer.isPlaying()) {
            avAudioPlayer.pause()
        }
        else {
            avAudioPlayer.play()
        }
    }

}

actual fun getAudioPlayer(file: KmpFile): AudioPlayer = IOSAudioPlayer(file)
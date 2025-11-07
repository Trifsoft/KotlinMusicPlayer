package org.trifsoft.musicplayer

import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.AVFoundation.AVMetadataCommonKeyTitle
import platform.AVFoundation.AVMetadataItem
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.commonKey
import platform.AVFoundation.commonMetadata
import platform.AVFoundation.stringValue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalForeignApi::class)
class IOSAudioPlayer(file: KmpFile): AudioPlayer {
    val avAudioPlayer = AVAudioPlayer(file.url, null)
    val avUrlAsset = AVURLAsset(file.url, null)
    init {
        avAudioPlayer.prepareToPlay()
    }
    override val duration: Float
        get() = avAudioPlayer.duration.seconds.inWholeSeconds.toFloat()
    override val title: String?
        = (avUrlAsset.commonMetadata as List<AVMetadataItem>).firstOrNull { it ->
            it.commonKey == AVMetadataCommonKeyTitle
        }?.stringValue

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
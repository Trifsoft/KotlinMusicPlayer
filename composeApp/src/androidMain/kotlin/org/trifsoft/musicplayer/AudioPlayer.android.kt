package org.trifsoft.musicplayer

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import com.mohamedrejeb.calf.io.KmpFile
import kotlin.math.roundToInt

class AndroidAudioPlayer(file: KmpFile): AudioPlayer {
    val mediaPlayer = MediaPlayer()
    val metadataRetriever = MediaMetadataRetriever().apply {
        setDataSource(MyApp.application, file.uri)
    }
    init {
        mediaPlayer.setDataSource(MyApp.application, file.uri)
        mediaPlayer.prepare()
    }
    
    override val duration: Float = mediaPlayer.duration / 1000f
    override val title: String? = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

    override fun seekTo(value: Float) {
        mediaPlayer.seekTo((value * 1000).roundToInt())
    }

    override fun playPause() {
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
        else {
            mediaPlayer.start()
        }
    }
}

actual fun getAudioPlayer(file: KmpFile): AudioPlayer = AndroidAudioPlayer(file)
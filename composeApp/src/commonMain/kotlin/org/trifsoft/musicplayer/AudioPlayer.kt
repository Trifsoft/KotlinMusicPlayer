package org.trifsoft.musicplayer

import com.mohamedrejeb.calf.io.KmpFile
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface AudioPlayer {

    val duration: Float
    fun seekTo(value: Float)
    fun playPause()
}

expect fun getAudioPlayer(file: KmpFile): AudioPlayer
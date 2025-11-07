package org.trifsoft.musicplayer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor
import kotlin.time.ExperimentalTime

val lightGrayColor = Color(0xFFCCCCCC)
val darkGrayColor = Color(0xFF888888)
val space = 10.dp

fun timeString(time: Float): String {
    val intTime = floor(time).toInt()
    val minutesString = if(intTime < 600) {
        "0${intTime/60}"
    } else {
        "${intTime/60}"
    }
    val secondsString = if(intTime % 60 < 10) {
        "0${intTime%60}"
    } else {
        "${intTime%60}"
    }
    return "$minutesString:$secondsString"
}

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun App() {

    var audioPlayer by remember { mutableStateOf<AudioPlayer?>(null, neverEqualPolicy()) }

    val filePicker = rememberFilePickerLauncher(
        type = FilePickerFileType.Audio,
        selectionMode = FilePickerSelectionMode.Single
    ) { kmpFiles ->
        if(kmpFiles.isNotEmpty()) {
            audioPlayer = getAudioPlayer(kmpFiles.first())
        }
    }

    MaterialTheme {
        Box(
            Modifier
                .safeDrawingPadding()
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(space)
            ) {
                audioPlayer.let { audioPlayer ->
                    if(audioPlayer == null) {
                        Text("Song not selected.")
                    }
                    else {
                        Player(audioPlayer)
                    }
                }
                Button(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A48F8),
                        contentColor = Color.White
                    ),
                    onClick = filePicker::launch,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Pick a song from storage")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun Player(audioPlayer: AudioPlayer) {
    var seekState by remember { mutableStateOf(Animatable(0f)) }
    val coroutineScope = rememberCoroutineScope()
    val startAnimation: suspend ()->Unit = {
        seekState.animateTo(
            audioPlayer.duration,
            animationSpec = tween(
                ((audioPlayer.duration - seekState.value) * 1000f).toInt(),
                easing = LinearEasing
            ),
        )
    }
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(lightGrayColor, RoundedCornerShape(20.dp))
    ) {
        Icon(
            imageVector = Icons.Filled.MusicNote,
            tint = darkGrayColor,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.5f)
                .align(Alignment.Center)
        )
    }
    audioPlayer.title?.let { title ->
        Text(
            text = title,
            fontSize = 25.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Slider(
        value = seekState.value,
        colors = SliderDefaults.colors(
            thumbColor = Color.Black
        ),
        track = { sliderState ->
            SliderDefaults.Track(
                colors = SliderDefaults.colors(
                    activeTrackColor = darkGrayColor,
                    inactiveTrackColor = lightGrayColor,
                ),
                drawStopIndicator = { },
                sliderState = sliderState
            )
        },
        onValueChange = {
            val isRunning = seekState.isRunning
            seekState = Animatable(it)
            if(isRunning) {
                coroutineScope.launch { startAnimation() }
            }
            audioPlayer.seekTo(it)
        },
        valueRange = 0f..audioPlayer.duration,
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(timeString(seekState.value))
        Text(timeString(audioPlayer.duration))
    }
    IconButton(
        onClick = {
            audioPlayer.playPause()
            coroutineScope.launch {
                if(seekState.isRunning) {
                    seekState.stop()
                }
                else {
                    startAnimation()
                }
            }
        }
    ) {
        Icon(
            imageVector = if(seekState.isRunning) {
                Icons.Filled.Pause
            } else {
                Icons.Filled.PlayArrow
            },
            contentDescription = null
        )
    }
}
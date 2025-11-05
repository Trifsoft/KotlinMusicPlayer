package org.trifsoft.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor

fun timeString(seekValue: Float): String {
    val intTime = floor(seekValue).toInt()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val space = 10.dp
    val lightGrayColor = Color(0xFFCCCCCC)
    val darkGrayColor = Color(0xFF888888)

    val trackDuration = 83f
    var seekValue by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("Lorem ipsum") }

    val coroutineScope = rememberCoroutineScope()

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
                Text(
                    text = title,
                    fontSize = 25.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Slider(
                    value = seekValue,
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
                            sliderState = sliderState)
                    },
                    onValueChange = { seekValue = it },
                    valueRange = 0f..trackDuration
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(timeString(seekValue))
                    Text(timeString(trackDuration))
                }
                IconButton(
                    onClick = { isPlaying = !isPlaying }
                ) {
                    Icon(
                        imageVector = if(isPlaying) {
                            Icons.Filled.Pause
                        } else {
                            Icons.Filled.PlayArrow
                        },
                        contentDescription = null
                    )
                }
                Button(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A48F8),
                        contentColor = Color.White
                    ),
                    onClick = {
                        coroutineScope.launch {
                            val file = FileKit.openFilePicker(type = FileKitType.File("mp3"))
                            file?.let { file ->
                                title = file.nameWithoutExtension
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Pick a song from storage")
                }
            }
        }
    }
}
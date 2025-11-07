# 🎵 KotlinMusicPlayer

A lightweight **Compose Multiplatform** music player app that allows users to pick and play audio files from their local storage.  
It demonstrates the use of **Calf File Picker** for selecting audio files and provides basic playback controls such as play/pause, scrubbing, and track progress visualization.

---

## ✨ Features

- 🎧 **Play Local Audio Files** — Uses the [Calf File Picker](https://github.com/MohamedRejeb/Calf) library to open the system file picker and select audio files.
- ⏯️ **Play / Pause Controls** — Simple and responsive playback management.
- ⏩ **Seek / Scrub Through Song** — Interactive slider to move through the track.
- 🕒 **Live Time Display** — Shows current playback time and total duration.
- 🎶 **Title Display** — Displays the loaded song’s title (if available).
- 🧩 **Multiplatform Compose UI** — Built using `composeApp` with Material 3 styling.

---

## 🧠 Design Decisions

### Audio Source
The app **does not stream or download music** — it relies entirely on **user-provided local files**.  
Songs are selected from the user’s device storage using the **Calf File Picker**, which abstracts platform-specific implementations for Android, iOS, Desktop, and Web.

### Architecture
- The app follows a **single-screen Compose design** with a `Player` composable handling UI and playback state.
- `AudioPlayer` is an abstraction that encapsulates the actual platform-specific audio playback logic.
- File selection is handled through:
  ```kotlin
  rememberFilePickerLauncher(
      type = FilePickerFileType.Audio,
      selectionMode = FilePickerSelectionMode.Single
  )
  ```
- The selected file is passed to a helper function `getAudioPlayer(kmpFiles.first())`, which creates an appropriate `AudioPlayer` instance (if a song is selected).

### Player State Handling

The player’s playback progress is **simulated using an `Animatable<Float, AnimationVector1D>` instance** named `seekState`:

- `seekState.value` — represents the current playback position within the track.
- `seekState.isRunning` — indicates whether playback is active (playing) or paused.
- `seekState.animateTo(audioPlayer.duration, animationSpec = tween(((audioPlayer.duration - seekState.value) * 1000f).toInt(), easing = LinearEasing))` — triggers continuous animation of the playback position while the track is playing.
- `seekState.stop()` — halts the animation when playback is paused.

Using an `Animatable` enables the **`Slider` composable** to smoothly and dynamically represent playback progress through animation.  
This design choice was made because platform-specific audio players generally provide only **stateless progress values**, which do not support continuous, animated UI updates in Compose.

---

## 🚀 How to Run

### Prerequisites
- Kotlin 2.0+
- JetBrains Compose Multiplatform setup
- Gradle (via `gradlew` wrapper)

## 🧰 Libraries Used

- **JetBrains Compose Multiplatform** — UI toolkit
- **Calf File Picker** — File picker for KMP (`com.mohamedrejeb.calf.picker`)
- **Material 3 Components** — Modern Compose design system
- **Kotlinx Coroutines** — For animation and async playback control
- **Kotlinx Animation** — For the seekbar progress animation

---

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:
- for the Wasm target (faster, modern browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```
- for the JS target (slower, supports older browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.
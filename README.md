# npu-agent

npu-agent is an experimental Android app that exposes a simple HTTP interface to an on-device “agent” running on the phone, intended to evolve into a controllable NPU‑accelerated inference and tooling layer. The initial target device is a Samsung Galaxy S20 Ultra (Exynos 990, 128 GB) running a Termux‑based development environment, but the project is structured as a standard Gradle Android app so others can build and run it from GitHub Actions artifacts or locally with the Android SDK.[web:1823][web:1827]

## Project status

- ✅ Builds successfully on GitHub Actions (`main` branch) and uploads a debug APK artifact (`npu-agent-debug-apk`) for others to download and install.[web:1770][web:1788][web:1809]  
- ✅ Headless `MainActivity` plus a stub `HttpServerService` implemented in Kotlin under the `ai.npuagent` package, providing the skeleton service that will later host the HTTP/JSON API for the agent.[web:1769]  
- ✅ Android/Gradle `.gitignore` added so the repository stays clean of build outputs and local Gradle state.[web:1796][web:1799]  
- ⏳ Next steps: flesh out the HTTP endpoints, plug in model execution on available accelerators (CPU/GPU/NPU), and define a stable JSON protocol for remote tools and agents.[web:1815][web:1818]

## Timeline and progress

- **Initial setup** – Import baseline Android app structure, Gradle configuration, and CI workflow to build a debug APK from `main`.[web:1805]  
- **Service refactor** – Create a headless `MainActivity` and introduce `HttpServerService` as a background service to eventually host the local HTTP API.[web:1769]  
- **Codebase cleanup** – Remove a legacy Java `HttpServerService` implementation whose invalid string handling caused CI compilation failures; keep the Kotlin service as the single source of truth.[web:1769]  
- **CI hardening** – Fix GitHub Actions Android build failures and standardize on a single debug build workflow that compiles the app and publishes the APK as an artifact on every successful run.[web:1770][web:1788][web:1807]  
- **Repository hygiene** – Add an Android‑focused `.gitignore` to exclude `.gradle`, `build/`, and other generated files so contributors see only meaningful changes in diffs and pull requests.[web:1796][web:1799]

## Building and running

- **From GitHub Actions**:  
  - Go to the **Actions** tab, open the latest successful “Build debug APK” run on `main`, and download the `npu-agent-debug-apk` artifact.[web:1788][web:1809]  
  - Install the resulting APK on an Android device with developer mode enabled.

- **From source (local)**:  
  - Clone the repo and open it in Android Studio, or run `./gradlew assembleDebug` with an appropriate Android SDK installed.[web:1790]  
  - The debug APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.

## Goals

- Provide a lightweight, script‑friendly agent endpoint over HTTP for Android devices, suitable for experimentation with on‑device AI, remote tools, and NPU‑accelerated workloads.[web:1815][web:1818][web:1821]  
- Keep the project approachable for contributors by using standard Android and GitHub Actions patterns and by ensuring every `main` commit is buildable with a published APK artifact.[web:1770][web:1788][web:1807]

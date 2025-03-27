# Android Native Project: Health Tracker

This project is a **native Android** application developed using **Kotlin** and **Jetpack Compose**, designed to evaluate and compare native Android development with a Kotlin Multiplatform (KMP) implementation. The app focuses on **health data visualization** and **Bluetooth device integration**, along with API integration for fetching additional health metrics.

## Project Structure

### Code Organization
* `/app`: Main application module containing all the Android-specific code.
  * UI Layer:
    * Built with Jetpack Compose.
    * Includes screens like Dashboard, History, Bluetooth Scanner, and Settings.
  * Domain Layer:
    * Business logic for processing and managing health data.
    * Use cases and models (e.g., `HealthData`, `BluetoothDevice`).
  * Data Layer:
    * API integration using Ktor or Retrofit for health metrics.
    * Bluetooth functionality using Android’s `BluetoothAdapter`.

### Key Components
* **Navigation**: Bottom Navigation Bar for switching between main sections.
* **Bluetooth Integration**: Scans and connects to Bluetooth devices using platform APIs.
* **Health Data Handling**: Models and visualizes historical health metrics.

## Features

### Core Features
* **Health Data Visualization**:
  * Displays graphs and trends for metrics like heart rate.
  * Fetches data from a mock or real API.
* **Bluetooth Device Management**:
  * Scan for nearby Bluetooth Low Energy (BLE) devices.
  * Connect to and interact with a selected device.
* **Search and Filter**:
  * Search and filter historical health records within the app.

## UI & UX
* Built with **Jetpack Compose** for a modern UI experience.
* Implements **Material Design** guidelines for consistent look and feel.

## Setup and Installation

### Prerequisites
Install the following tools:
* **Android Studio (Giraffe or newer)** for development and testing.
* A physical Android device or emulator with Bluetooth capability for testing Bluetooth features.

### Running the Project
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and build the project.
4. Run the app on a device or emulator.

## Evaluation Goals

This project is part of a broader comparison with a Kotlin Multiplatform version. It is intended to assess:

1. **Development Speed**:
   * Evaluate how quickly features can be built using native Android.
2. **App Size**:
   * Measure the final APK size and compare with KMP.
3. **Performance**:
   * Assess the responsiveness of Bluetooth operations, API calls, and UI transitions.

## Contributing

Contributions are welcome! If you find issues or have suggestions, feel free to open an issue or submit a pull request.

---

This README provides a clear overview of the native Android project, its structure, goals, and setup process for other developers or evaluators.

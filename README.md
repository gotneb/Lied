<div align="center">
    <h1>Lied Music Player</h1>
    <a><img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white&style=for-the-badge"/></a>
    <a><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-904bf9.svg?logo=kotlin&logoColor=white&style=for-the-badge"/></a>
    <a><img alt="Jetpack Compose" src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white&style=for-the-badge"></a>
    <p>Offline music player, developed using Jetack Compose</p>
    <img src="assets/header.png" alt="drawing" width="80%"/>
</div>

## Architecture
This project follows the **MVI** architecture pattern to separate concerns and promote modularity.
- State Management: ViewModels manage UI states efficiently, ensuring responsive and clean UI updates.
- Separation of Concerns: Clear distinction between UI logic, business logic, and data layers.
- Dependency Injection: **Koin** handles dependencies across the app, making it easier to test and manage.

# Contributing to P8 Multimedia Project

Welcome to the **Mobile Programming Practicum #8 (Multimedia)** repository! We appreciate your interest in checking out or contributing to this project. This app demonstrates multimedia handling in Android using Jetpack Compose.

## Prerequisites

Before you begin, ensure you have the following installed to run the project successfully:
* **Android Studio**: Latest stable version recommended.
* **JDK**: Version 17.
* **Android SDK**: Minimum API 24 (Android 7.0), Target API 34.

## Project Structure

Please follow the existing package structure when adding new features:
* `ui/`: Contains all Composable screens (home, gallery, player, recorder, video).
* `util/`: Helper classes (e.g., FileManagerUtility).
* `theme/`: UI styling (Color, Type, Theme).

## How to Contribute

1.  **Fork** the repository to your own GitHub account.
2.  **Clone** the project to your local machine.
3.  Create a new **Branch** for your feature or bugfix:
    ```bash
    git checkout -b feature/amazing-feature
    ```
4.  **Make your changes**. Ensure you are using **Jetpack Compose** for UI and respecting the **Material 3** design guidelines.
5.  **Commit** your changes using descriptive messages:
    * Use `feat:` for new features (e.g., "feat: implement zoom gesture").
    * Use `fix:` for bug fixes (e.g., "fix: camera permission crash").
    * Use `docs:` for documentation updates.
6.  **Push** to your branch:
    ```bash
    git push origin feature/amazing-feature
    ```
7.  Open a **Pull Request** targeting the `main` branch.

## Reporting Issues

If you encounter bugs (e.g., ExoPlayer errors, permission denials) or have ideas for improvements, please use the tab. Describe the issue clearly and include steps to reproduce it.

## Coding Standards

* Code must be written in **Kotlin**.
* Follow the official [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide).
* Ensure no unused imports or resources remain before committing.

Happy Coding!

# TestDynamicAndroid

This project is an Android application that demonstrates user authentication using email and OTP verification, powered by the Dynamic SDK.

## Architecture

The application follows a feature-based architecture, where the code is organized by feature instead of by layer. This approach groups all the related components of a feature (e.g., UI, ViewModel, dependency injection) together, making the codebase easier to navigate and maintain.

The main architectural components are:

*   **Package by Feature** Each feature located in feature/ directory and has it's own independent logic
*   **CLEAN** Each feature has own data, domain and presentation ensuring a clear separation of concerns and maintainability.
*   **Model-View-ViewModel (MVVM):** The UI is built using Jetpack Compose, and the state is managed by ViewModels.
*   **Dependency Injection:** Hilt is used for dependency injection, which simplifies the management of dependencies and improves testability.
*   **Single Activity:** The application uses a single-activity architecture, where different screens are implemented as composable functions.

## Features

- ✅ **Pull-to-refresh on Wallet Details** The wallet details screen includes a pull-to-refresh mechanism to allow users to manually refresh their wallet information.
- ✅ **Jetpack Compose UI:** The entire UI is built using modern, declarative Jetpack Compose.
- ✅ **MVVM Architecture:** A clear separation of concerns between the UI and business logic.
- ✅ **Kotlin Coroutines + StateFlow:** Asynchronous operations are handled efficiently using coroutines, with UI state exposed via `StateFlow`.
- ✅ **Dynamic SDK Integration:** Seamless integration with the Dynamic SDK for core authentication functionalities.
- ✅ **Email OTP Authentication:** A complete and working email and OTP verification flow.
- ✅ **Input Validation:** Client-side validation for inputs like email addresses to provide immediate feedback.
- ✅ **Error Handling:** Backend errors are caught, converted into human-readable messages, and displayed to the user.
- ✅ **Loading States:** The UI provides clear loading indicators during asynchronous operations.
- ✅ **Hilt DI:** Manages dependencies throughout the application.
- ✅ **Material Design 3:** The app uses the latest Material Design components and theming.

## Error Handling and Validation

The application includes robust error handling and input validation. When errors are received from the backend (e.g., during authentication), they are caught, converted into a human-readable format, and displayed to the user. This ensures that the user is always informed about what's happening.

Client-side input validation is also implemented (e.g., for email formats) to provide immediate feedback to the user and prevent invalid data from being sent in the first place.

## How to Run

To run the application, you will need to provide your Dynamic SDK environment ID.

1.  **Clone the repository.**
2.  **Create a `local.properties` file** in the root of the project.
3.  **Add your Dynamic environment ID** to the `local.properties` file:
    ```properties
    ENV_ID=your_dynamic_env_id
    ```
4.  **Open the project in Android Studio** and sync the Gradle files.
5.  **Run the application** on an emulator or a physical device.

## Screenshots

*This section is a placeholder for screenshots of the application.*

## Assumptions

* i assume that sdk handles all the backend communication, key management,
and security aspects of the authentication process, allowing developer to focus on the error validation and user experience.

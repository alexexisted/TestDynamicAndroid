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
![photo_2026-02-08 17 58 09](https://github.com/user-attachments/assets/3827e393-4b2c-4faa-b268-323046265b78)

![photo_2026-02-08 17 58 14](https://github.com/user-attachments/assets/2884ea86-828e-4374-a00d-899ece4e82e2)

![photo_2026-02-08 17 58 20](https://github.com/user-attachments/assets/39ba4dd7-6c25-49c1-933b-8c65fd958f30)

![photo_2026-02-08 17 58 24](https://github.com/user-attachments/assets/7fb21e0f-ffdb-4296-b0b0-ffe9f006fa9a)

![photo_2026-02-08 17 58 26](https://github.com/user-attachments/assets/0bcbe21f-e2b5-477e-93d1-6527b055012d)


## Assumptions

* i assume that sdk handles all the backend communication, key management,
and security aspects of the authentication process, allowing developer to focus on the error validation and user experience.

# NextNonce

Welcome to the official repository for the **NextNonce Mobile App**. NextNonce is an on-chain crypto portfolio tracker for both Android and iOS.

This application is built with **Kotlin Multiplatform** to ensure a consistent and high-quality experience across both platforms from a single, shared codebase. It's designed to be simple and intuitive, making it the perfect portfolio tracker for crypto beginners and regular users.


## 📲 Download

The Android app is available for download directly from GitHub releases:

* **[Download for Android](https://github.com/NextNonce/Mobile/releases)**


## ✨ Features

* **Completely Safe & Non-Custodial:** Security is a top priority. NextNonce **never** asks for private keys or seed phrases. The app works by tracking public wallet addresses only, meaning user assets are always under their control.
* **Unified Asset View:** The standout feature simplifies cross-chain tracking. NextNonce automatically groups the most popular tokens (like ETH, USDT, USDC, BNB) from all supported networks into a single unified view. See the total sum of each asset across all chains, and simply tap to see a detailed breakdown of the balance on each network.
* **Broad Blockchain Support:** Track assets across **15+** of the most popular EVM blockchains. Support is constantly being added for more EVM and non-EVM chains in the future.
* **Real-time Updates:** Prices and balances are updated in real-time for the most current view of your portfolio.
* **Universal Wallet Support:** The app tracks balances for both **Simple** wallets (EOA) and **Smart** wallets.
* **Cross-Platform:** A single codebase for both Android and iOS using **Kotlin Multiplatform** and **Compose Multiplatform**.
* **Secure Authentication:** Sign in with email or Google, powered by **Supabase**.
* **Clean & Modern UI:** A sleek and intuitive interface designed with Material Design principles. The app includes fully implemented light and dark themes, ensuring a polished and consistent user experience across the entire application.


## 📖 Documentation

For detailed information, please refer to the official documentation site:

- **[User Guide](https://docs.nextnonce.com/user/)**
  _Everything needed to get the most out of the NextNonce app._

- **[Developer Docs](https://docs.nextnonce.com/developer/mobile/)**
  _Get started with the codebase, understand the architecture._


## 🛠️ Tech Stack & Architecture

NextNonce is built with a modern, scalable, and maintainable tech stack, following the principles of **Clean Architecture** and **MVVM**.

* **Core:**
  * [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)
  * [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous operations.
* **UI (Shared):**
  * [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) for declarative UI.
  * [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) for screen routing.
* **Architecture:**
  * **Clean Architecture** (`data`, `domain`, `presentation` layers).
  * **Model-View-ViewModel (MVVM)** pattern.
  * [Koin](https://insert-koin.io/) for dependency injection.
* **Networking:**
  * [Ktor Client](https://www.google.com/search?q=https://ktor.io/docs/client-overview.html) for all network requests.
  * [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for JSON parsing.
* **Authentication:**
  * [Supabase](https://supabase.com/) for authentication and backend services.
* **Database:**
  * [Room](https://developer.android.com/training/data-storage/room) for local on-device caching.
* **Image Loading:**
  * [Kamel](https://github.com/Kamel-Media/Kamel) for asynchronous image loading in Compose Multiplatform.
* **Logging:**
  * [Kermit](https://kermit.touchlab.co/) for shared, multiplatform logging.


## 🚀 Getting Started

To build and run the project locally, the environment must be configured.

### Prerequisites

- Android Studio (latest stable version).
    
- **On macOS:**
  
  -  Xcode.
    
  - **Highly Recommended:** [KDoctor](https://github.com/Kotlin/kdoctor) for diagnosing your setup.
    

### Configuration

1. **Clone the repository:**
    
    Bash
    
    ```
    git clone https://github.com/NextNonce/Mobile.git
    cd Mobile
    ```
    
2. **Create `local.properties` file:** Create a `local.properties` file in the root directory of the project. This file is essential for providing API keys and environment-specific variables required by the app.
    
3. **Add Configuration Keys:** Copy the contents of `local.properties.example` and paste them into the new `local.properties` file. Your own values must be provided for the following properties:
    
    Properties
    
    ```shell
    # Supabase Credentials
    SUPABASE_URL="YOUR_SUPABASE_URL"
    SUPABASE_ANON_KEY="YOUR_SUPABASE_ANON_KEY"
    
    # Google Auth for Supabase
    GOOGLE_WEB_CLIENT_ID="YOUR_GOOGLE_WEB_CLIENT_ID"
    ```
    

### Running the App

The process for running the application depends on your operating system.


#### On macOS (for Android & iOS)

1. **Check Environment:** The easiest way to ensure your environment is set up correctly is by using **KDoctor**. Open your terminal and run the following commands:
    
    ```shell
    # Install KDoctor via Homebrew (if you don't have it)
    brew install kdoctor
    
    # Run KDoctor and check the output
    kdoctor
    ```
    
    KDoctor will analyze your system and provide instructions if any part of your setup (like Xcode, command-line tools, or Android Studio) needs attention. Address all issues it reports.
    
2. **Build in Android Studio:** Once KDoctor reports a clean environment, open the project in Android Studio. You can now run both platforms directly from the IDE:
    
    - For **iOS**, select the `iosApp` configuration and choose an available iOS Simulator or a connected physical device.
        
    - For **Android**, select the `composeApp` configuration and choose an Android emulator or a connected device.
        


#### On Windows / Linux (for Android only)

1. **Open Project:** Launch Android Studio and open the project folder.
    
2. **Build `composeApp`:** Android Studio will handle the download and setup of the necessary Android SDKs.
    
3. **Run:** Select the `composeApp` configuration and run it on an Android emulator or a connected physical device.

## 📂 Project Structure

The project follows a feature-driven, multi-layered modular structure that separates concerns and promotes scalability.

```
composeApp/src/commonMain/kotlin/com/nextnonce/app
├── auth/             # User authentication (Sign In/Up)
│   ├── data/
│   ├── domain/
│   └── presentation/
├── core/             # Core components (networking, database, models, DI)
│   ├── data/
│   ├── domain/
│   └── presentation/
├── home/             # Main home screen
│   └── presentation/
├── portfolio/        # Portfolio management and display
│   ├── data/
│   ├── domain/
│   └── presentation/
├── wallet/           # Wallet details and balance tracking
│   ├── data/
│   ├── domain/
│   └── presentation/
├── start/            # Initial screen to route signed-in/out users
│   └── presentation/
├── App.kt            # Main entry point with navigation graph
└── ...
```

* **`domain`**: Contains the core business logic, models, and use cases. It is pure Kotlin and has no platform dependencies.
* **`data`**: Implements the repositories defined in the `domain` layer and contains data sources (remote and local).
* **`presentation`**: Contains the UI (Compose screens) and ViewModels that interact with the domain layer's use cases.


## 📄 License

This project is licensed under the **MIT License**. See the `LICENSE` file for more details.

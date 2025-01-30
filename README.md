# Case Study News App - Android Application

A modern news application that provides real-time headlines and articles using NewsAPI. Built with cutting-edge Android development practices, this project ensures both performance and a great user experience.

## ✨ Features at a Glance

### 🏗 Architecture

- **Multi-module setup**: Enables better scalability and modularization.

- **Clean Architecture**: Layered design ensuring maintainability and testability.

- **MVVM (Model-View-ViewModel)**: Separates concerns for a structured and predictable codebase.

### 📶 Offline-First Approach

- **Retrofit**: Fetches news articles efficiently.

- **Room Database**: Caches news for offline reading.

- **Single Source of Truth**: Ensures consistency between local and remote data.

### 📚 Modern Libraries & Tech Stack

- **Hilt**: Dependency Injection for better scalability and testability.

- **Jetpack Compose**: Declarative UI with less boilerplate.

- **Firebase Analytics**: Track user engagement and app performance.

- **Paging 3**: Efficient pagination of news articles.

- **Coil**: Fast and smooth image loading.

- **Lazy Loading (LazyColumn)**: Renders large lists seamlessly.

- **Coroutines & Flow**: Ensures smooth, non-blocking operations.

### 🎨 Beautiful & Intuitive UI

- **Dark Mode**: Supports system-wide dark mode.

- **Designed in Figma**: Well-crafted UI for an immersive reading experience.

- **Splash Screen**: Smooth startup transition.

## 📸 Screenshots (Coming Soon)

## 🏗 Project Structure

📂 app\
📂 core\
┣ 📂 data        # Data sources (API, Database)\
┣ 📂 domain      # Business logic and use cases\
┣ 📂 presentation # UI (Compose screens, ViewModels)\
📂 news\
┣ 📂 data        # Data sources (API, Database)\
┣ 📂 domain      # Business logic and use cases\
┣ 📂 presentation # UI (Compose screens, ViewModels)

## 🚀 Getting Started

### Prerequisites

- Android Studio Koala

- Minimum SDK 31

- Gradle 8.5.0

## Setup

```
git clone https://github.com/yourusername/news-app.git
cd news-app
gradlew build
```

## 📌 Roadmap

## 🤝 Contributions

Contributions, issues, and feature requests are welcome! Feel free to open an issue or a pull request.

## 📜 License

MIT License - Free to use and modify.
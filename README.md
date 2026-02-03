# 👤 MyUserApp - Firebase Authentication System

Aplikasi Android modern yang mengimplementasikan sistem autentikasi lengkap menggunakan Firebase Authentication dan Firestore Database dengan arsitektur Clean Architecture.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## ✨ Features

### Authentication
-  **Login Screen**: Email & password authentication
-  **Registration Screen**: New user sign up with validation
-  **Forgot Password**: Password reset via email
-  **Real-time Validation**: Visual feedback saat input

### Home Page
-  **User Profile**: Display name & email verification status
-  **User List**: Real-time list dari Firestore dengan Shimmer Effect
-  **Filter**: By email verification status (All/Verified/Not Verified)
-  **Search**: By name atau email dengan instant search
-  **Pull to Refresh**: Manual refresh user data

### Email Verification
-  **Auto Send**: Verification email terkirim saat registrasi
-  **UI Update**: Badge status berubah sesuai verifikasi
-  **Real-time Sync**: Auto-sync dengan Firebase Auth

### Password Reset
-  **Reset via Email**: Kirim link reset password
-  **Dedicated UI**: Screen khusus password reset
-  **Email Validation**: Validasi format email

### Unit Tests
-  **Use Case Tests**: SignIn, SignUp, Password Reset
-  **ViewModel Tests**: Login, Register, Home

## 🏗️ Architecture

Project ini menggunakan **Clean Architecture** dengan **MVVM Pattern**:
```
app/
├── data/                      # Data Layer
│   ├── source/
│   │   ├── remote/           # Firebase Auth & Firestore
│   │   └── repository/       # Repository implementations
│   └── Resource.kt           # Result wrapper
│
├── domain/                    # Business Logic Layer
│   ├── model/                # Domain models
│   ├── repository/           # Repository interfaces
│   └── usecase/              # Business use cases
│
├── presentation/              # UI Layer
│   ├── auth/                 # Authentication screens
│   ├── home/                 # Home screen
│   ├── navigation/           # Navigation setup
│   └── components/           # Reusable UI components
│
├── di/                       # Dependency Injection
└── utils/                    # Utilities & Extensions
```

### Design Patterns & Principles

- ✅ **MVVM**: ViewModel + UI State
- ✅ **Clean Architecture**: Separation of Concerns
- ✅ **SOLID Principles**: Single Responsibility, Open/Closed, dll
- ✅ **Repository Pattern**: Data abstraction
- ✅ **Use Case Pattern**: Business logic encapsulation
- ✅ **Dependency Injection**: Hilt untuk DI

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin 2.2.10 |
| **UI Framework** | Jetpack Compose |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Hilt |
| **Async** | Coroutines + Flow |
| **State Management** | StateFlow |
| **Backend** | Firebase (Auth + Firestore) |
| **Testing** | JUnit, MockK, Turbine |

## 📦 Installation

### Option 1: Clone Repository
```bash
# Clone repository
git clone https://github.com/divaamwall/diva_amwal_maulana_supian_mdtest.git
```

### Option 2: Download APK

📱 **Download APK**: https://drive.google.com/file/d/1-THu3wvC7mm7MyZTMdzVdvmX2xvPLO6t/view?usp=sharing
```
1. Download APK dari link di atas
2. Install di Android device
3. Buka aplikasi dan mulai gunakan!
```

## ⚙️ Setup

### 1. Firebase Configuration

#### A. Create Firebase Project
1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik "Add project"
3. Masukkan nama project
4. Ikuti wizard setup

#### B. Add Android App
1. Klik icon Android di project overview
2. **Package name**: `com.diva.myuserapp`
3. Download `google-services.json`
4. Letakkan di folder `app/`

#### C. Enable Authentication
1. Di Firebase Console → **Authentication**
2. Klik "Get started"
3. Tab "Sign-in method"
4. Enable **Email/Password**
5. Save

#### D. Create Firestore Database
1. Di Firebase Console → **Firestore Database**
2. Klik "Create database"
3. Start in **test mode**
4. Pilih location terdekat
5. Enable

#### E. Security Rules
Paste rules ini di Firestore:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### 2. Build Project
```bash
# Sync Gradle
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install ke device/emulator
./gradlew installDebug
```

### 3. Run Tests
```bash
# Run all unit tests
./gradlew test

# Run with coverage
./gradlew testDebugUnitTestCoverage

# View HTML report
open app/build/reports/tests/testDebugUnitTest/index.html
```

## 🎯 Usage

### 1. Register
```
1. Buka app → Klik "Register"
2. Isi: Name, Email, Password
3. Validasi real-time akan muncul
4. Klik "REGISTER" saat semua validasi ditandai dengan ceklis
5. Check email untuk verifikasi
```

### 2. Verify Email
```
1. Buka email dari Firebase
2. Klik link verifikasi
3. Kembali ke app
```

### 3. Login
```
1. Masukkan email & password
2. Klik "LOGIN"
3. Otomatis masuk ke Home Screen
```

### 4. Home Features
```
- Pull down untuk refresh
- Search user by name/email
- Filter by verification status
- Lihat profile sendiri di Top App Bar
```

## 🧪 Testing

### Run Tests
```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests "SignInUseCaseTest"

# With coverage report
./gradlew testDebugUnitTestCoverage
```

## 📂 Project Structure
```
com.diva.myuserapp/
├── data/
│   ├── source/
│   │   ├── remote/
│   │   │   ├── FirebaseAuthDataSource.kt
│   │   │   ├── FirestoreDataSource.kt
│   │   │   └── response/UserResponse.kt
│   │   └── repository/
│   │       ├── AuthRepositoryImpl.kt
│   │       └── UserRepositoryImpl.kt
│   └── Resource.kt
│
├── domain/
│   ├── model/User.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   └── UserRepository.kt
│   └── usecase/
│       ├── SignInUseCase.kt
│       ├── SignUpUseCase.kt
│       ├── GetAllUsersUseCase.kt
│       └── ...
│
├── presentation/
│   ├── auth/
│   │   ├── login/
│   │   ├── register/
│   │   └── forgot_password/
│   ├── home/
│   └── navigation/
│
├── di/
│   ├── FirebaseModule.kt
│   └── RepositoryModule.kt
│
└── utils/
    ├── Constants.kt
    └── Extensions.kt
```

## 🚀 Key Features Detail

### Real-time Validation
- Validasi input saat user mengetik
- Visual feedback dengan icon & color
- Button auto-enable/disable

### State Management
- Immutable UI State
- StateFlow untuk reactive updates
- Clean state transitions

### Error Handling
- User-friendly error messages
- AlertDialog untuk errors

## 🔒 Security

- ✅ Firebase Authentication
- ✅ Firestore Security Rules
- ✅ Password minimum 6 characters
- ✅ Email format validation
- ✅ No sensitive data in logs

## 📱 Requirements

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36 (Android 15)
- **Compile SDK**: 36



## 👨‍💻 Author

**[Diva Amwal Maulana Supian]**
- Email: m.sdiva524@gmail.com
- LinkedIn: [[LinkedIn](https://www.linkedin.com/in/divaamwall/)]
- Portfolio: [[Portfolio](https://diva-portofolio.vercel.app/)]

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Firebase for backend services
- Jetpack Compose for modern UI
- Hilt for dependency injection
- Material 3 Design System

---

- **Note**: This is a technical test project showcasing Clean Architecture, MVVM, and modern Android development practices.
- Made with ❤️ using Kotlin & Jetpack Compose

---

# 📱 Rumah Aman - Aplikasi Pendamping Korban Kekerasan

<div align="center">

![Rumah Aman Logo](app/src/main/res/drawable/logo_512.png)

**Aplikasi mobile Android untuk membantu korban kekerasan menemukan layanan bantuan yang tepat**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-11.0.0-orange.svg)](https://firebase.google.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[Fitur](#-fitur-utama) • [Teknologi](#-teknologi) • [Instalasi](#-instalasi) • [Struktur](#-struktur-project) • [Kontribusi](#-kontribusi)

</div>

---

## 📖 Tentang Proyek

**Rumah Aman** adalah aplikasi mobile yang dirancang untuk membantu korban kekerasan (fisik, verbal, psikologis, atau seksual) menemukan layanan bantuan yang sesuai dengan kebutuhan mereka. Aplikasi ini menggunakan **sistem rekomendasi berbasis Firestore** dan **AI Chat** untuk memberikan dukungan yang personal dan cepat.

### 🎯 Tujuan
- Memberikan akses mudah ke layanan bantuan (psikologi, hukum, medis)
- Sistem rekomendasi otomatis berdasarkan profil dan kebutuhan pengguna
- Edukasi melalui tips keamanan dan artikel terkait
- AI Chat untuk konsultasi awal dan support 24/7

---

## ✨ Fitur Utama

### 🔐 **Autentikasi & Manajemen Akun**
- ✅ Login dengan email & password
- ✅ Register akun baru dengan validasi
- ✅ Reset password via email
- ✅ Edit profil (nama, nomor HP, email)
- ✅ Ubah password dengan validasi re-authentication

### 🏠 **Dashboard Interaktif**
- ✅ Welcome message dengan nama pengguna (real-time dari Firestore)
- ✅ Quick access ke fitur utama:
  - 📝 Sistem Rekomendasi
  - 🤖 AI Chat
  - 📖 Artikel & Tips
- ✅ **Tips Populer** (2 tips dengan view counter tertinggi)
  - Real-time listener dari Firestore
  - Counter views otomatis terupdate
  - Skeleton loading untuk UX yang smooth

### 🔔 **Halaman Notifikasi**
- ✅ 3 kategori notifikasi:
  - **Welcome**: Selamat datang + CTA ke Settings
  - **Tips**: Tips keamanan dengan link eksternal
  - **Alert**: Peringatan penting
- ✅ Group by date (Hari ini, Kemarin, 7 hari yang lalu)
- ✅ Clickable card dengan validasi link
- ✅ Error handling untuk link invalid

### 🎯 **Sistem Rekomendasi Layanan**
- ✅ Form input multi-step dengan validasi:
  - Nama (otomatis dari profil user)
  - Jenis kelamin (Perempuan/Laki-laki)
  - Umur (13-100 tahun, number input)
  - Provinsi (dropdown - hardcoded: Jawa Timur)
  - Jenis kekerasan (Fisik/Verbal/Psikologis/Seksual)
  - Jenis pelayanan (Psikologi/Hukum/Medis)
- ✅ Query cerdas ke Firestore berdasarkan kriteria
- ✅ Hasil rekomendasi dengan detail:
  - Nama lembaga
  - Deskripsi layanan
  - Alamat
  - Kontak (telepon, email, website)
  - Tags

### 🤖 **AI Chat dengan Groq**
- ✅ Powered by **Llama 3.3 70B** via Groq API
- ✅ Response time <2 detik
- ✅ Context-aware conversation
- ✅ UI chat modern dengan:
  - Bubble chat (user vs AI)
  - Typing indicator
  - Auto-scroll
  - Error handling
- ✅ Streaming response (real-time)

### ⚙️ **Pengaturan & Profil**
- ✅ Tampilan profil user (nama, email, HP)
- ✅ Menu navigasi:
  - Edit Profil
  - Ubah Password
  - Tentang Aplikasi
  - Syarat & Ketentuan
  - Kebijakan Privasi
  - Logout dengan konfirmasi
- ✅ Real-time data sync dengan Firestore
- ✅ Auto-create user document untuk akun baru

---

## 🛠️ Teknologi yang Digunakan

### **Frontend (Mobile)**
| Teknologi | Versi | Deskripsi |
|-----------|-------|-----------|
| **Kotlin** | 1.9.0 | Bahasa pemrograman utama |
| **Jetpack Compose** | 1.5.0 | Modern UI toolkit |
| **Material 3** | Latest | Design system |
| **Navigation Compose** | 2.7.0 | Navigasi antar screen |
| **Hilt** | 2.48 | Dependency injection |
| **Coroutines** | 1.7.3 | Asynchronous programming |
| **StateFlow** | Latest | State management |

### **Backend & Cloud Services**
| Service | Fungsi |
|---------|--------|
| **Firebase Authentication** | Login/Register dengan email |
| **Cloud Firestore** | Database NoSQL untuk users, tips, recommendations |
| **Groq API** | AI Chat dengan Llama 3.3 70B |

### **Libraries Tambahan**
- **Coil** - Image loading
- **OkHttp** - HTTP client untuk Groq API
- **Gson** - JSON parsing

---

## 🗃️ Struktur Database Firestore

```
firestore/
│
├── users/                          # Collection user profiles
│   └── {uid}/                      # Document per user (UID dari Firebase Auth)
│       ├── name: String
│       ├── email: String
│       ├── phoneNumber: String
│       ├── gender: String          # "Perempuan" / "Laki-laki"
│       ├── age: String
│       ├── province: String
│       └── createdAt: Timestamp
│
├── tips/                           # Collection tips keamanan
│   └── {tipId}/
│       ├── title: String
│       ├── description: String
│       ├── link: String
│       ├── viewCount: Int          # Counter berapa kali dilihat
│       ├── createdAt: Timestamp
│       └── imageRes: String (optional)
│
├── recommendations/                # Collection layanan bantuan
│   └── {recommendationId}/
│       ├── input/                  # Kriteria matching
│       │   ├── gender: String
│       │   ├── province: String
│       │   ├── violence: String    # "FISIK", "VERBAL", "PSIKOLOGIS", "SEKSUAL"
│       │   └── need: String        # "PSIKOLOGI", "HUKUM", "MEDIS"
│       └── service/                # Detail layanan
│           ├── name: String
│           ├── description: String
│           ├── type: String
│           ├── address: String
│           ├── contact/
│           │   ├── phone: String
│           │   ├── email: String
│           │   └── url: String
│           ├── tags: Array<String>
│           └── lastUpdated: String
│
└── notifications/ (optional)       # Future: Push notifications
```

### 📊 Contoh Data

**User Document:**
```json
{
  "name": "Margaretha",
  "email": "margarethap@gmail.com",
  "phoneNumber": "+62 899 3651 888",
  "gender": "Perempuan",
  "age": "25",
  "province": "Jawa Timur",
  "createdAt": "2025-12-06T10:30:00Z"
}
```

**Tip Document:**
```json
{
  "title": "Latihan Pernapasan Yang Bisa Meredakan Kecemasan",
  "description": "Teknik pernapasan 4-7-8 untuk menenangkan pikiran",
  "link": "https://example.com/tips/breathing",
  "viewCount": 24,
  "createdAt": "2025-12-01T08:00:00Z"
}
```

**Recommendation Document:**
```json
{
  "input": {
    "gender": "Perempuan",
    "province": "Jawa Timur",
    "violence": "VERBAL",
    "need": "PSIKOLOGI"
  },
  "service": {
    "name": "Biro Psikologi Lestari",
    "description": "Konseling psikologis, dukungan trauma ringan-sedang",
    "type": "PSIKOLOGI",
    "address": "Jl. Nginden Semolo No.21, Surabaya",
    "contact": {
      "phone": "+62-812-0000-0001",
      "email": "admin@birolestari.id",
      "url": "https://birolestari.id"
    },
    "tags": ["trauma", "konseling", "verbal-abuse"],
    "lastUpdated": "2025-11-10"
  }
}
```

---

## 🤖 Integrasi Groq API

Aplikasi menggunakan **Groq Cloud** dengan model **Llama 3.3 70B Versatile** untuk AI Chat yang cepat dan akurat.

### Setup API Key

1. Daftar di [Groq Console](https://console.groq.com)
2. Dapatkan API key
3. Tambahkan ke `local.properties`:
```properties
GROQ_API_KEY=gsk_xxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

### Implementasi Request

```kotlin
// Di ChatRepository.kt
suspend fun sendMessage(message: String): Result<String> {
    val payload = """
    {
      "model": "llama-3.3-70b-versatile",
      "messages": [
        {
          "role": "system",
          "content": "Kamu adalah asisten AI untuk aplikasi Rumah Aman yang membantu korban kekerasan."
        },
        {
          "role": "user",
          "content": "$message"
        }
      ],
      "temperature": 0.7,
      "max_tokens": 1000
    }
    """.trimIndent()

    val request = Request.Builder()
        .url("https://api.groq.com/openai/v1/chat/completions")
        .addHeader("Authorization", "Bearer ${BuildConfig.GROQ_API_KEY}")
        .addHeader("Content-Type", "application/json")
        .post(payload.toRequestBody("application/json".toMediaType()))
        .build()

    // ...handle response
}
```

### Performance
- ⚡ **Response time**: 500ms - 2s
- 🚀 **Throughput**: 300+ tokens/second
- 💬 **Context window**: 32K tokens

---

## 📂 Struktur Project

```
app/
├── data/
│   ├── model/                      # Data classes
│   │   ├── User.kt
│   │   ├── Tip.kt
│   │   └── Recommendation.kt
│   └── repository/                 # Repository pattern
│       ├── UserRepository.kt
│       ├── TipsRepository.kt
│       ├── RecommendationRepository.kt
│       └── ChatRepository.kt
│
├── domain/
│   └── usecase/                    # Business logic
│       ├── SaveUserDataUseCase.kt
│       ├── GetRecommendationUseCase.kt
│       └── SendChatMessageUseCase.kt
│
├── presentation/                   # UI Layer
│   ├── auth/
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   └── LoginViewModel.kt
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   ├── recommendation/
│   │   ├── RecommendationScreen.kt
│   │   ├── RecommendationResultScreen.kt
│   │   └── RecommendationViewModel.kt
│   ├── chat/
│   │   ├── ChatScreen.kt
│   │   └── ChatViewModel.kt
│   ├── notification/
│   │   ├── NotificationScreen.kt
│   │   └── NotificationViewModel.kt
│   ├── pengaturan/
│   │   ├── PengaturanScreen.kt
│   │   └── PengaturanViewModel.kt
│   ├── editProfile/
│   │   ├── EditProfileScreen.kt
│   │   └── EditProfileViewModel.kt
│   ├── editPassword/
│   │   ├── EditPasswordScreen.kt
│   │   └── EditPasswordViewModel.kt
│   └── ui/                         # Reusable components
│       ├── TipCard.kt
│       ├── NotificationCard.kt
│       ├── TextFieldAuth.kt
│       └── theme/
│           └── Color.kt
│
├── navigation/
│   ├── NavGraph.kt                 # Root navigation
│   ├── MainScreenNavGraph.kt       # Bottom nav navigation
│   └── Routes.kt
│
├── di/                             # Dependency injection
│   ├── AppModule.kt
│   └── RepositoryModule.kt
│
└── utils/
    ├── Constants.kt
    └── Extensions.kt
```

---

## 🚀 Cara Menjalankan Proyek

### Prerequisites
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17
- Android SDK 34
- Emulator atau device fisik (Android 7.0+)

### Setup Firebase

1. Buat project baru di [Firebase Console](https://console.firebase.google.com)
2. Tambahkan aplikasi Android:
   - Package name: `com.example.rumahaman`
   - Download `google-services.json`
3. Letakkan file di: `app/google-services.json`
4. Enable **Authentication** (Email/Password)
5. Buat database **Cloud Firestore**

### Setup Groq API

1. Daftar di [Groq Console](https://console.groq.com)
2. Generate API key
3. Tambahkan ke `local.properties`:
```properties
GROQ_API_KEY=your_groq_api_key_here
```

### Clone & Run

```bash
# Clone repository
git clone https://github.com/DBizma/PAPB-Rumah-Aman.git
cd PAPB-Rumah-Aman

# Buka di Android Studio
# File → Open → Pilih folder project

# Atau via command line
studio .

# Tunggu Gradle sync selesai
# Run aplikasi
./gradlew installDebug
```

### Build APK

```bash
# Debug build
./gradlew assembleDebug

# Release build (memerlukan keystore)
./gradlew assembleRelease
```

APK akan tersedia di: `app/build/outputs/apk/`

---

## 🔒 Keamanan & Privasi

- ✅ Password di-hash oleh Firebase Authentication
- ✅ Firestore Rules membatasi akses per user (UID-based)
- ✅ API Key disimpan di `local.properties` (tidak di-commit)
- ✅ HTTPS untuk semua komunikasi
- ✅ No sensitive data logged

### Firestore Security Rules (Contoh)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // User hanya bisa akses data mereka sendiri
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Tips bisa dibaca semua user yang login
    match /tips/{tipId} {
      allow read: if request.auth != null;
      allow update: if request.auth != null && 
                       request.resource.data.diff(resource.data).affectedKeys()
                       .hasOnly(['viewCount']);
    }
    
    // Recommendations bisa dibaca semua user yang login
    match /recommendations/{recId} {
      allow read: if request.auth != null;
    }
  }
}
```

---

## 🐛 Known Issues & Limitations

- [ ] Tips Populer hanya support filter untuk provinsi "Jawa Timur"
- [ ] AI Chat tidak menyimpan conversation history (setiap session baru)
- [ ] Skeleton loading kadang flicker saat data cepat load
- [ ] Belum ada fitur bookmark atau save recommendations

---

## 🗺️ Roadmap

### v1.1 (Next Release)
- [ ] Multi-language support (EN/ID)
- [ ] Dark mode
- [ ] Save conversation history
- [ ] Bookmark layanan favorit
- [ ] Push notifications

### v2.0 (Future)
- [ ] Support lebih banyak provinsi
- [ ] Peta interaktif (Google Maps) untuk lokasi layanan
- [ ] Forum komunitas (anonymous)
- [ ] Voice message di AI Chat
- [ ] Export chat transcript ke PDF

---

## 🤝 Kontribusi

Kontribusi sangat diterima! Silakan fork repository ini dan submit pull request.

### Cara Berkontribusi

1. Fork repository
2. Buat branch baru (`git checkout -b feature/AmazingFeature`)
3. Commit perubahan (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

### Code Style
- Ikuti [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Gunakan meaningful variable names
- Tambahkan comments untuk logic yang kompleks
- Update README jika menambah fitur baru

---

## 🙏 Acknowledgments

- [Firebase](https://firebase.google.com) - Backend as a Service
- [Groq](https://groq.com) - Ultra-fast AI inference
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- [Material Design 3](https://m3.material.io) - Design system
- Semua kontributor yang telah membantu proyek ini

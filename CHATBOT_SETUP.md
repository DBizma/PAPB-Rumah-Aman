# Fitur Chat Bot - RumahAman

## 📝 Ringkasan
Fitur Chat Bot telah dibuat dengan struktur Clean Architecture yang lengkap.

## 🏗️ Struktur File yang Dibuat

### Domain Layer
1. **ChatMessage.kt** - Model data untuk pesan chat
2. **ChatBotRepository.kt** - Interface repository
3. **SendMessageUseCase.kt** - Use case untuk mengirim pesan

### Data Layer
4. **ChatBotRepositoryImpl.kt** - Implementasi repository dengan Gemini API

### Presentation Layer
5. **ChatBotUiState.kt** - State management untuk UI
6. **ChatBotViewModel.kt** - ViewModel dengan logic bisnis
7. **ChatBotScreen.kt** - UI Screen dengan tampilan chat
8. **DashboardScreen.kt** - Dashboard sederhana dengan tombol ke ChatBot

### Dependency Injection
9. **AppModule.kt** - Updated dengan provider Gemini AI

### Navigation
10. **NavGraph.kt** - Updated dengan route CHATBOT_SCREEN
11. **MainScreenNavGraph.kt** - Updated dengan DashboardScreen

## ⚙️ Setup yang Perlu Dilakukan

### 1. Dapatkan API Key Gemini
1. Kunjungi: https://makersuite.google.com/app/apikey
2. Buat API Key baru
3. Copy API Key

### 2. Update AppModule.kt
Ganti `YOUR_GEMINI_API_KEY` dengan API Key Anda di file:
`app/src/main/java/com/example/rumahaman/di/AppModule.kt`

```kotlin
@Provides
@Singleton
fun provideGenerativeModel(): GenerativeModel {
    return GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "PASTE_YOUR_API_KEY_HERE" // Ganti ini
    )
}
```

### 3. Sync Gradle
- Klik "Sync Project with Gradle Files" di Android Studio
- Tunggu sampai dependency Gemini AI terdownload

## 🎨 Fitur

### Dashboard Screen
- Tampilan sederhana dengan tombol "Chatting Bot"
- Navigate ke ChatBot Screen saat tombol diklik

### ChatBot Screen
- **Header** dengan icon chatbot
- **Chat area** dengan bubble message:
  - User (Margareth) - Warna orange, di kanan
  - Chatbot - Warna teal, di kiri
- **Input field** dengan tombol send
- **Loading indicator** saat menunggu response
- **Auto scroll** ke pesan terbaru
- **Pesan sambutan** otomatis: "Haloo Margareth"

## 🔄 Alur Kerja

1. User membuka Dashboard
2. Klik tombol "Chatting Bot"
3. ChatBot Screen terbuka dengan pesan sambutan
4. User mengetik pesan di input field
5. Tekan tombol send (atau Enter)
6. Pesan user muncul di chat bubble (orange, kanan)
7. Loading indicator muncul
8. API Gemini memproses pesan dengan context sebagai konselor
9. Response bot muncul di chat bubble (teal, kiri)
10. User bisa melanjutkan percakapan

## 🎯 Context Prompt untuk Gemini

Chatbot dikonfigurasi dengan context khusus:
- Berperan sebagai konselor untuk korban kekerasan seksual
- Memberikan dukungan emosional dan informasi
- Bersikap empati dan profesional
- Tidak memberikan diagnosis medis atau legal advice
- Mengarahkan ke bantuan profesional jika diperlukan

## 📱 Testing

1. Build & Run aplikasi
2. Login dengan akun Anda
3. Di Dashboard, klik tombol "Chatting Bot"
4. Test kirim pesan:
   - "Apa aja sih jenis kekerasan seksual?"
   - "Saya merasa tertekan, apa yang harus saya lakukan?"
   - "Bagaimana cara melaporkan kekerasan seksual?"

## ⚠️ Catatan Penting

1. **API Key Security**: 
   - Jangan commit API key ke git
   - Gunakan BuildConfig atau local.properties untuk production

2. **Internet Permission**: 
   - Sudah ada di AndroidManifest.xml

3. **Error Handling**:
   - Jika API error, chatbot akan kirim pesan "Maaf, saya mengalami masalah teknis"
   - Error tidak akan crash aplikasi

4. **Performance**:
   - Response time tergantung koneksi internet
   - Loading indicator memberikan feedback ke user

## 🎨 Warna Tema

- Chatbot bubble: #008B8B (Teal)
- User bubble: #FF9F87 (Orange)
- Background: #E8F5F5 (Light Cyan)
- Send button: #008B8B (Teal)

## 🚀 Next Steps (Opsional)

1. Tambahkan gambar chatbot custom (ganti emoji 🤖)
2. Simpan history chat ke Firestore
3. Tambahkan typing indicator animation
4. Support untuk mengirim gambar
5. Share conversation feature
6. Multi-language support

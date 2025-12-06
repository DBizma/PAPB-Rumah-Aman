# 📱 PAPB – Rumah Aman

Aplikasi mobile Android berbasis **Kotlin + Jetpack Compose** dengan fitur utama keamanan rumah, sistem pelaporan, dan AI Chat yang berjalan menggunakan **Groq API**.
Aplikasi ini menggunakan **Firebase Authentication** untuk login/register, dan **Cloud Firestore** sebagai database inti.

---

## ✨ Fitur Aplikasi

* 🔐 **Autentikasi Pengguna**

  * Login
  * Register
  * Reset password
* 🏠 **Dashboard Rumah Aman**
* 🔔 **Halaman Notifikasi**
* ⚙️ **Pengaturan Aplikasi**
* 👤 **Edit Profil Pengguna**
* 🔑 **Ubah Password**
* 📝 **Laporan & Masukkan**

  * Data disimpan ke Firestore
* 🤖 **AI Chat** dengan Groq API
* 🎨 Desain modern menggunakan Material 3 + Jetpack Compose

---

## 🛠️ Teknologi yang Digunakan

### **Mobile App**

* Kotlin
* Jetpack Compose
* Navigation Compose
* ViewModel + StateFlow
* Coroutines
* Material 3

### **Backend / Cloud**

* Firebase Authentication
* Cloud Firestore
* Groq API (LLM untuk AI Chat)

---

## 🗃️ Struktur Firestore (Sesuai Screenshot)

Firestore-mu mempunyai **5 collection utama**:

```
laporan/
   {documentId}/
      laporan: String
      timestamp: Long
      uid: String

masukkan/
   {documentId}/
      message: String
      timestamp: Long
      uid: String

recommendations/
   {documentId}/
      text: String
      createdAt: Long

tips/
   {documentId}/
      title: String
      description: String

users/
   {uid}/
      name: String
      email: String
      photoUrl: String (optional)
```

✔ Collection `laporan` → Menyimpan laporan user
✔ Field yang kamu gunakan (sesuai screenshot):

* `laporan`
* `timestamp`
* `uid`

Tidak ada subcollection tambahan — aman.

---

## 🤖 Integrasi Groq API (AI Chat)

Aplikasi memanfaatkan model Groq seperti **Llama 3 / Mixtral** untuk memberi respon AI cepat dan efisien.

Contoh request di Kotlin:

```kotlin
val payload = """
{
  "model": "llama3-8b-8192",
  "messages": [
    { "role": "user", "content": "$message" }
  ]
}
""".trimIndent()

val request = Request.Builder()
    .url("https://api.groq.com/openai/v1/chat/completions")
    .addHeader("Authorization", "Bearer ${BuildConfig.GROQ_KEY}")
    .post(payload.toRequestBody("application/json".toMediaType()))
    .build()
```

---

## 🔐 Firebase Authentication

Digunakan untuk:

* register email & password
* login
* mendapatkan UID untuk setiap laporan
* proteksi data Firestore
* update profil pengguna

Contoh mendapatkan UID:

```kotlin
val uid = FirebaseAuth.getInstance().currentUser?.uid
```

---

## 📂 Struktur Folder Proyek

```
app/
 ├─ data/
 │   ├─ firestore/      -> akses Firestore
 │   ├─ repository/     
 │   └─ model/          
 ├─ domain/
 │   ├─ entity/
 │   └─ usecase/
 ├─ ui/
 │   ├─ screens/
 │   ├─ components/
 │   └─ theme/
 ├─ utils/
 └─ di/
```

---

## 🚀 Cara Menjalankan Proyek

1. Clone:

   ```bash
   git clone https://github.com/SutaSS/PAPB-Rumah-Aman.git
   ```
2. Buka di Android Studio.
3. Tambahkan file:

   ```
   app/google-services.json
   ```
4. Tambahkan key Groq ke:

   ```
   local.properties
   GROQ_API_KEY=your_key_here
   ```
5. Jalankan aplikasi.

---

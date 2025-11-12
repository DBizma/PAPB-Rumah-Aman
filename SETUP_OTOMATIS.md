# 🚀 Setup Otomatis - Sistem Rekomendasi

## ✨ Cara Tercepat (3 Langkah!)

### 1️⃣ Ubah Start Destination (Sementara)

Buka `MainActivity.kt` dan ubah:

```kotlin
AppNavHost(
    navController = navController,
    startDestination = Routes.DATA_SEEDER_SCREEN // <- Ubah ke ini
)
```

### 2️⃣ Jalankan Aplikasi

- Build dan run aplikasi
- Akan muncul screen "Setup Data Rekomendasi"
- Klik tombol **"Upload Data ke Firestore"**
- Tunggu sampai muncul pesan sukses ✅

### 3️⃣ Kembalikan Start Destination

Ubah kembali di `MainActivity.kt`:

```kotlin
AppNavHost(
    navController = navController,
    startDestination = Routes.SPLASH_SCREEN // <- Kembalikan
)
```

**SELESAI!** 🎉 Data sudah ada di Firestore

---

## 📦 Apa yang Akan Diupload?

9 data rekomendasi lengkap:

| Gender | Violence | Service | Layanan |
|--------|----------|---------|---------|
| Perempuan | Verbal | Psikologi | Biro Psikologi Lestari |
| Perempuan | Verbal | Hukum | LBH Suara Adil |
| Perempuan | Verbal | Psikologi & Hukum | Pusat Layanan Terpadu Harmoni |
| Laki-laki | Fisik | Psikologi | Klinik Trauma Recovery Sejahtera |
| Laki-laki | Fisik | Hukum | Unit Pendampingan Hukum Amanah |
| Laki-laki | Fisik | Psikologi & Hukum | Pusat Krisis Terpadu Sehati |
| Perempuan | Fisik & Verbal | Psikologi | Rumah Konseling Pelita |
| Perempuan | Fisik & Verbal | Hukum | Posko Bantuan Hukum Tangguh |
| Perempuan | Fisik & Verbal | Psikologi & Hukum | Sentra Layanan Terpadu Mandiri |

Semua data untuk **Provinsi: Jawa Timur**

---

## 🔐 Firestore Rules (PENTING!)

Sebelum upload, set rules ini di Firebase Console:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /recommendations/{recommendationId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null; // Untuk upload
    }
  }
}
```

⚠️ **Setelah selesai upload, ubah jadi**:
```javascript
allow write: if false; // Security: hanya read saja
```

---

## 🧪 Testing

### Test 1: Cek Firestore Console
1. Buka Firebase Console
2. Firestore Database → `recommendations` collection
3. Harus ada 9 documents

### Test 2: Test di Aplikasi
```
Dashboard → Sistem Rekomendasi
Isi form:
  ✅ Nama: Margaretha
  ✅ Gender: Wanita  
  ✅ Umur: 20
  ✅ Provinsi: Jawa Timur
  ✅ Violence: Verbal
  ✅ Service: Psikologi
Klik "Tampilkan Rekomendasi"
→ Hasil: Biro Psikologi Lestari ✅
```

---

## 🗑️ Cleanup (Opsional)

Setelah data terupload, bersihkan code development:

### Hapus/Comment di `NavGraph.kt`:
```kotlin
// composable(Routes.DATA_SEEDER_SCREEN) {
//     DataSeederScreen()
// }
```

### Hapus Route Constant (Opsional):
```kotlin
// const val DATA_SEEDER_SCREEN = "data_seeder"
```

### File yang Bisa Dihapus:
- ❌ `DataSeederScreen.kt` (UI untuk upload)
- ⚠️ `RecommendationDataSeeder.kt` (biarkan untuk backup)

---

## 🎯 Mapping Reference

| UI Display | Firestore Value |
|-----------|-----------------|
| Wanita | Perempuan |
| Pria | Laki-laki |
| Fisik | FISIK |
| Verbal | VERBAL |
| Fisik & Verbal | FISIK_VERBAL |
| Psikologi | PSIKOLOGI |
| Hukum | HUKUM |
| Psikologis & Hukum | PSIKOLOGI_HUKUM |

---

## ❌ Troubleshooting

### Error: "Permission denied"
- ✅ Cek Firestore rules: `allow write: if request.auth != null`
- ✅ Pastikan sudah login (Firebase Auth)

### Error: "Network error"
- ✅ Cek koneksi internet
- ✅ Cek `google-services.json` terbaru

### Data tidak muncul di app
- ✅ Cek Firestore Console (data ada?)
- ✅ Input Provinsi harus exact: "Jawa Timur"
- ✅ Lihat Logcat untuk error detail

---

## 📝 File Baru yang Dibuat

```
✅ RecommendationModels.kt - Data models
✅ RecommendationRepository.kt - Firestore query
✅ RecommendationViewModel.kt - State management
✅ RecommendationDataSeeder.kt - Auto upload data
✅ DataSeederScreen.kt - UI untuk trigger upload
✅ Updated NavGraph.kt - Navigation routes
✅ Updated RecommendationScreen.kt - Form dengan ViewModel
✅ Updated RecommendationResultScreen.kt - Display hasil
```

---

## 🎉 Summary

**3 langkah simple:**
1. Ubah startDestination → DATA_SEEDER_SCREEN
2. Run app → Upload
3. Kembalikan startDestination → SPLASH_SCREEN

**No manual upload!** ⚡
**No Firebase Console!** 🚫
**Just 1 click!** 🖱️

Enjoy! 🚀

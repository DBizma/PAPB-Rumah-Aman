# 🔍 Troubleshooting Sistem Rekomendasi

## ✅ Perbaikan yang Sudah Dilakukan

### 1. **Navigasi "Back" Stuck** ✅ FIXED
**Masalah:** Ketika klik back dari RecommendationResultScreen, tidak bisa kembali ke Dashboard

**Solusi:**
- Mengubah `navController.popBackStack()` menjadi `navController.popBackStack(Routes.DASHBOARD, inclusive = false)`
- Ini akan langsung kembali ke Dashboard dan clear seluruh backstack
- Menambahkan `viewModel.clearRecommendation()` sebelum navigasi untuk clean state

**Lokasi File:**
- `navigation/NavGraph.kt` - Line ~68-81

### 2. **Data Tidak Muncul** 🔄 NEED TESTING
**Kemungkinan Penyebab:**
1. Format data di Firestore tidak match dengan query
2. Firestore rules masih terlalu strict
3. User tidak terautentikasi

**Solusi yang Diterapkan:**
- Menambahkan logging detail di `RecommendationRepository.kt`
- Menambahkan logging di `RecommendationViewModel.kt`
- Pesan error lebih informatif

---

## 📊 Cara Debug Data Tidak Muncul

### Step 1: Check Logcat
Jalankan app dan cari rekomendasi, lalu lihat Logcat:

```
Filter: RecommendationViewModel | RecommendationRepository
```

**Yang Harus Muncul:**
```
D/RecommendationViewModel: Getting recommendation with:
D/RecommendationViewModel:   Name: [nama user]
D/RecommendationViewModel:   Gender: Perempuan
D/RecommendationViewModel:   Province: Jawa Timur
D/RecommendationViewModel:   Violence Type: FISIK
D/RecommendationViewModel:   Service Type: PSIKOLOGI

D/RecommendationRepository: Searching with:
D/RecommendationRepository:   gender: Perempuan
D/RecommendationRepository:   violence: FISIK
D/RecommendationRepository:   need: PSIKOLOGI
D/RecommendationRepository:   province: Jawa Timur

D/RecommendationRepository: Query returned 1 documents
D/RecommendationRepository: Document data: {...}
D/RecommendationRepository: Successfully parsed recommendation: [Nama Layanan]
```

### Step 2: Verify Firestore Data Structure

Data di Firestore **HARUS** persis seperti ini:

```javascript
recommendations (collection)
  └── rec-perempuan-fisik-psikologi-jawatimur-001 (document)
       ├── recommendationId: "rec-perempuan-fisik-psikologi-jawatimur-001"
       ├── input: (map)
       │    ├── gender: "Perempuan"        ⚠️ HARUS persis
       │    ├── violence: "FISIK"          ⚠️ HARUS uppercase
       │    ├── need: "PSIKOLOGI"          ⚠️ HARUS uppercase
       │    └── province: "Jawa Timur"     ⚠️ HARUS persis (spasi + huruf besar)
       └── service: (map)
            ├── name: "..."
            ├── type: "..."
            ├── description: "..."
            ├── address: "..."
            ├── contact: (map)
            │    ├── url: "..."
            │    ├── phone: "..."
            │    └── email: "..."
            ├── logoUrl: null (atau string)
            ├── tags: ["..."] (array)
            └── lastUpdated: "2024-01-15"
```

### Step 3: Check Firestore Rules

Pastikan rules sudah di-update:

```javascript
match /recommendations/{recommendationId} {
  allow read: if request.auth != null;
  allow write: if false;
}
```

### Step 4: Check User Authentication

User **HARUS** sudah login. Cek dengan:
```kotlin
FirebaseAuth.getInstance().currentUser != null
```

---

## 🧪 Test Cases

### Test 1: Perempuan + Fisik + Psikologi
```
Input:
  Nama: Test User
  Gender: Wanita (→ "Perempuan")
  Umur: 25
  Provinsi: Jawa Timur
  Kekerasan: Fisik (→ "FISIK")
  Layanan: Psikologi (→ "PSIKOLOGI")

Expected Result: ✅ Harus muncul 1 rekomendasi
```

### Test 2: Laki-laki + Verbal + Hukum
```
Input:
  Gender: Pria (→ "Laki-laki")
  Kekerasan: Verbal (→ "VERBAL")
  Layanan: Hukum (→ "HUKUM")
  Provinsi: Jawa Timur

Expected Result: ✅ Harus muncul 1 rekomendasi
```

### Test 3: Data Tidak Ada
```
Input:
  Province: Jakarta (tidak ada di database)

Expected Result: ❌ Error: "Tidak ada rekomendasi yang sesuai dengan kriteria Anda"
```

---

## 🔧 Common Issues

### Issue 1: "Query returned 0 documents"

**Kemungkinan:**
- Provinsi salah ketik (harus "Jawa Timur" bukan "jawa timur" atau "Jawatimur")
- Violence/Service type tidak uppercase
- Gender tidak match ("Perempuan" vs "perempuan")

**Fix:**
Cek data di Firebase Console → Firestore → recommendations → pilih salah satu document → lihat field `input.*`

### Issue 2: "Failed to parse recommendation"

**Kemungkinan:**
- Structure Firestore tidak sesuai dengan data class
- Ada field yang missing (terutama nested objects)

**Fix:**
Compare struktur di Firestore dengan `RecommendationModels.kt`

### Issue 3: PERMISSION_DENIED

**Kemungkinan:**
- User belum login
- Firestore rules belum di-update

**Fix:**
1. Update Firestore rules (lihat file sebelumnya)
2. Pastikan user sudah login
3. Test dengan user lain

---

## 📝 Mapping Input → Query

| UI Display | Kotlin Value | Firestore Field |
|-----------|--------------|-----------------|
| "Wanita" | "Perempuan" | `input.gender: "Perempuan"` |
| "Pria" | "Laki-laki" | `input.gender: "Laki-laki"` |
| "Fisik" | "FISIK" | `input.violence: "FISIK"` |
| "Verbal" | "VERBAL" | `input.violence: "VERBAL"` |
| "Fisik & Verbal" | "FISIK_VERBAL" | `input.violence: "FISIK_VERBAL"` |
| "Psikologi" | "PSIKOLOGI" | `input.need: "PSIKOLOGI"` |
| "Hukum" | "HUKUM" | `input.need: "HUKUM"` |
| "Psikologis & Hukum" | "PSIKOLOGI_HUKUM" | `input.need: "PSIKOLOGI_HUKUM"` |
| (User input) | (String) | `input.province: "Jawa Timur"` |

---

## 🚀 Next Steps

1. **Run the app** dan test dengan input yang sesuai
2. **Check Logcat** untuk melihat query parameters
3. **Verify Firestore** data structure jika masih error
4. **Check Authentication** status user

Jika masih ada masalah, bagikan screenshot Logcat atau error message! 🔍

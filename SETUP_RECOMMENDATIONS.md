# Panduan Setup Sistem Rekomendasi

## 1. Upload Data ke Firestore

### Langkah-langkah:

1. **Buka Firebase Console**
   - Login ke https://console.firebase.google.com/
   - Pilih project "Rumah Aman"

2. **Buat Collection**
   - Pilih menu "Firestore Database"
   - Klik "Start collection"
   - Nama collection: `recommendations`

3. **Upload Data JSON**
   
   Untuk setiap item dalam JSON, buat document dengan struktur:

   **Document ID**: `rec-perempuan-verbal-psikologi-jatim-01` (gunakan recommendation_id sebagai ID)

   **Fields**:
   ```
   recommendation_id: "rec-perempuan-verbal-psikologi-jatim-01"
   
   input (map):
       ├─ gender: "Perempuan"
       ├─ violence: "VERBAL"
       ├─ need: "PSIKOLOGI"
       └─ province: "Jawa Timur"
   
   service (map):
       ├─ name: "Biro Psikologi Lestari"
       ├─ type: "PSIKOLOGI"
       ├─ description: "Konseling psikologis, dukungan trauma..."
       ├─ address: "The Serenity, Jl. Nginden Semolo No.21..."
       ├─ contact (map):
       │   ├─ url: "https://contoh.id/biro-lestari"
       │   ├─ phone: "+62-812-0000-0001"
       │   └─ email: "admin@birolestari.id"
       ├─ logo_url: null
       ├─ tags (array): ["trauma", "konseling", "verbal-abuse"]
       └─ last_updated: "2025-11-10"
   ```

4. **Cara Cepat - Import via Firebase CLI** (Opsional)
   
   Atau bisa menggunakan script untuk upload otomatis:

   ```javascript
   // Simpan file ini sebagai uploadRecommendations.js
   const admin = require('firebase-admin');
   const serviceAccount = require('./serviceAccountKey.json');
   const recommendations = require('./recommendations.json'); // JSON data Anda

   admin.initializeApp({
     credential: admin.credential.cert(serviceAccount)
   });

   const db = admin.firestore();

   async function uploadData() {
     const batch = db.batch();
     
     recommendations.forEach(rec => {
       const docRef = db.collection('recommendations').doc(rec.recommendation_id);
       batch.set(docRef, rec);
     });

     await batch.commit();
     console.log('Data berhasil di-upload!');
   }

   uploadData();
   ```

   Jalankan dengan: `node uploadRecommendations.js`

## 2. Struktur Kode yang Sudah Dibuat

### Models (`RecommendationModels.kt`)
- `RecommendationInput`: Data input user (gender, violence, need, province)
- `ServiceContact`: Kontak layanan (url, phone, email)
- `RecommendationService`: Detail layanan lengkap
- `RecommendationResult`: Hasil rekomendasi final

### Repository (`RecommendationRepository.kt`)
- `getRecommendation()`: Query berdasarkan input user
- `getAllRecommendations()`: Ambil semua data (untuk testing)

### ViewModel (`RecommendationViewModel.kt`)
- Manage state form dan hasil rekomendasi
- Handle loading dan error states
- Fungsi update untuk setiap field

### UI Components
- **RecommendationScreen**: Form input dengan validasi
- **RecommendationResultScreen**: Tampilan hasil dengan Scaffold

## 3. Mapping Data

### Gender:
- UI: "Wanita" / "Pria"
- Firestore: "Perempuan" / "Laki-laki"

### Violence Type:
- UI: Fisik / Verbal / Fisik & Verbal
- Firestore: "FISIK" / "VERBAL" / "FISIK_VERBAL"

### Service Type:
- UI: Psikologi / Hukum / Psikologis & Hukum
- Firestore: "PSIKOLOGI" / "HUKUM" / "PSIKOLOGI_HUKUM"

### Province:
- Input langsung dari user
- Harus match persis dengan data di Firestore
- Contoh: "Jawa Timur"

## 4. Testing

### Test Manual di Firestore Console:
1. Buka Firestore Console
2. Filter collection `recommendations`
3. Pastikan ada minimal 1 document
4. Test query manual dengan field yang sesuai

### Test di Aplikasi:
1. Isi form dengan data:
   - Nama: (bebas)
   - Gender: Wanita
   - Umur: (bebas)
   - Provinsi: Jawa Timur
   - Violence: Verbal
   - Service: Psikologi

2. Klik "Tampilkan Rekomendasi"
3. Seharusnya muncul loading dialog
4. Kemudian navigate ke hasil dengan data "Biro Psikologi Lestari"

## 5. Troubleshooting

### Error: "Tidak ada rekomendasi yang sesuai"
- Pastikan input match persis dengan data Firestore
- Check spelling province (case-sensitive)
- Pastikan field `input` dalam Firestore berisi map dengan field yang benar

### Error: Connection issues
- Check internet connection
- Pastikan Firebase sudah dikonfigurasi dengan benar
- Check `google-services.json` sudah terbaru

### Loading terus menerus
- Check Logcat untuk error detail
- Pastikan Firestore rules allow read:
  ```
  rules_version = '2';
  service cloud.firestore {
    match /databases/{database}/documents {
      match /recommendations/{document=**} {
        allow read: if request.auth != null;
      }
    }
  }
  ```

## 6. Firestore Rules

Tambahkan rules berikut di Firebase Console > Firestore > Rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow authenticated users to read recommendations
    match /recommendations/{recommendationId} {
      allow read: if request.auth != null;
      allow write: if false; // Only admin can write via console/backend
    }
  }
}
```

## 7. Next Steps

1. ✅ Upload semua 9 data recommendations ke Firestore
2. ✅ Test aplikasi dengan berbagai kombinasi input
3. 🔄 (Opsional) Tambah fitur search/autocomplete untuk province
4. 🔄 (Opsional) Tambah logo untuk setiap service
5. 🔄 (Opsional) Clickable link untuk URL dan email

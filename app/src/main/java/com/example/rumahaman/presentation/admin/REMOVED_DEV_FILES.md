# Development Files - ARCHIVED

## Files Removed for Production

The following development files have been archived after successful data upload:

### 1. DataSeederScreen.kt
- **Purpose:** UI screen for uploading initial recommendation data to Firestore
- **Status:** ✅ Completed successfully - 9 recommendations uploaded
- **Location:** `presentation/admin/DataSeederScreen.kt`
- **Reason for Removal:** Development-only tool, no longer needed in production

### 2. RecommendationDataSeeder.kt
- **Purpose:** Backend logic for batch uploading 9 hardcoded recommendations
- **Status:** ✅ Completed successfully
- **Location:** `data/repository/RecommendationDataSeeder.kt`
- **Reason for Removal:** Development-only tool, security risk if left in production

## Production Data Status

✅ **Firestore Collection: `recommendations`**
- 9 documents successfully uploaded
- Combinations: Gender (2) × Violence Type (3) × Service Type (3) × Province (1 - Jawa Timur)
- Document IDs format: `rec-{gender}-{violence}-{need}-{province}-{number}`

## Security Notes

- Firestore rules updated to **read-only** for recommendations collection
- Write access: Admin only via Firebase Console
- Read access: Authenticated users only

## Future Data Updates

To add/update recommendations in production:
1. Use Firebase Console
2. Navigate to Firestore Database → `recommendations` collection
3. Manually add/edit documents following the RecommendationResult model structure

---
**Date Archived:** 2024
**Data Upload Date:** Successfully completed before production deployment

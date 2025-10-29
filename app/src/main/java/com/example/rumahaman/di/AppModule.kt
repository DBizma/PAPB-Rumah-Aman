package com.example.rumahaman.di

import com.example.rumahaman.data.repository.AuthRepositoryImpl
import com.example.rumahaman.data.repository.TipsRepositoryImpl
import com.example.rumahaman.domain.repository.AuthRepository
import com.example.rumahaman.domain.repository.TipsRepository
import com.google.firebase.auth.FirebaseAuth // <-- 1. IMPORT FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Provider untuk Firestore (sudah benar) ---
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- Provider untuk TipsRepository (sudah benar) ---
    @Provides
    @Singleton
    fun provideTipsRepository(firestore: FirebaseFirestore): TipsRepository {
        return TipsRepositoryImpl(firestore)
    }

    // --- PERBAIKAN DI SINI ---

    // 2. Tambahkan provider untuk FirebaseAuth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // 3. Terima FirebaseAuth sebagai parameter untuk membuat AuthRepositoryImpl
    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }
}

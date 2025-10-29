package com.example.rumahaman.di

import com.example.rumahaman.data.repository.TipsRepositoryImpl
import com.example.rumahaman.domain.repository.TipsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideTipsRepository(firestore: FirebaseFirestore): TipsRepository {
        return TipsRepositoryImpl(firestore)
    }

    // Nanti, tambahkan provider untuk repository lain di sini
    // @Provides
    // @Singleton
    // fun provideAuthRepository(...) : AuthRepository { ... }
}

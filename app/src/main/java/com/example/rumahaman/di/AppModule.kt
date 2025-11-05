package com.example.rumahaman.di

import com.example.rumahaman.data.remote.GroqApiService
import com.example.rumahaman.data.repository.AuthRepositoryImpl
import com.example.rumahaman.data.repository.ChatBotRepositoryImpl
import com.example.rumahaman.data.repository.ChatHistoryRepositoryImpl
import com.example.rumahaman.data.repository.TipsRepositoryImpl
import com.example.rumahaman.data.repository.UserRepositoryImpl
import com.example.rumahaman.domain.repository.AuthRepository
import com.example.rumahaman.domain.repository.ChatBotRepository
import com.example.rumahaman.domain.repository.ChatHistoryRepository
import com.example.rumahaman.domain.repository.TipsRepository
import com.example.rumahaman.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Properties
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val GROQ_BASE_URL = "https://api.groq.com/"
    
    // API Key dengan fallback robust - baca dari BuildConfig atau local.properties
    private val GROQ_API_KEY: String by lazy {
        try {
            // Coba baca dari BuildConfig (akan ter-generate setelah build)
            val buildConfigClass = Class.forName("com.example.rumahaman.BuildConfig")
            val field = buildConfigClass.getField("GROQ_API_KEY")
            field.get(null) as? String ?: getApiKeyFromLocalProperties()
        } catch (e: Exception) {
            // Fallback: baca langsung dari local.properties
            getApiKeyFromLocalProperties()
        }
    }
    
    private fun getApiKeyFromLocalProperties(): String {
        return try {
            val properties = Properties()
            val rootDir = System.getProperty("user.dir") ?: ""
            val localPropertiesFile = java.io.File(rootDir, "local.properties")
            
            if (localPropertiesFile.exists()) {
                properties.load(localPropertiesFile.inputStream())
                properties.getProperty("GROQ_API_KEY", "")
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

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

    // 4. Provider untuk UserRepository
    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    // 4b. Provider untuk ChatHistoryRepository
    @Provides
    @Singleton
    fun provideChatHistoryRepository(firestore: FirebaseFirestore): ChatHistoryRepository {
        return ChatHistoryRepositoryImpl(firestore)
    }

    // 5. Provider untuk OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 6. Provider untuk Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GROQ_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 7. Provider untuk GroqApiService
    @Provides
    @Singleton
    fun provideGroqApiService(retrofit: Retrofit): GroqApiService {
        return retrofit.create(GroqApiService::class.java)
    }

    // 8. Provider untuk Groq API Key
    @Provides
    @Singleton
    @Named("groqApiKey")
    fun provideGroqApiKey(): String = GROQ_API_KEY

    // 9. Provider untuk ChatBotRepository
    @Provides
    @Singleton
    fun provideChatBotRepository(
        groqApiService: GroqApiService,
        @Named("groqApiKey") apiKey: String
    ): ChatBotRepository {
        return ChatBotRepositoryImpl(groqApiService, apiKey)
    }
}

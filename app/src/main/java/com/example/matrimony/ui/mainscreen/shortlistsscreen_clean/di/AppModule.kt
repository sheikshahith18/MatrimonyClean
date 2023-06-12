package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.di

import android.app.Application
import androidx.room.Room
import com.example.matrimony.db.AppDatabase
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.db.AppDatabase1
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.repositories.*
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.*
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.*
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.GetConnectionStatusUseCaseImpl
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.GetSettingsUseCaseImpl
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.GetUserAlbumCountUseCaseImpl
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.GetUserDataUseCasesImpl
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase.GetShortlistedProfilesUseCaseImpl
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase.RemoveShortlistUseCaseImpl
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase.ShortlistsUseCases
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
    fun provideAppDatabase(application: Application): AppDatabase1 {
        return Room.databaseBuilder(application, AppDatabase1::class.java, "app_database1").build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: AppDatabase): UserRepository {
        return UserRepositoryImpl(db.userDao())
    }
//
    @Provides
    @Singleton
    fun provideConnectionsRepository(db: AppDatabase): ConnectionsRepository {
        return ConnectionsRepositoryImpl(db.connectionsDao())
    }

    @Provides
    @Singleton
    fun provideShortlistsRepository(db: AppDatabase): ShortlistsRepository {
        return ShortlistsRepositoryImpl(db.shortlistsDao())
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(db: AppDatabase): AlbumRepository {
        return AlbumRepositoryImpl(db.albumDao())
    }
    @Provides
    @Singleton
    fun providePrivacySettingsRepository(db: AppDatabase): PrivacySettingsRepository {
        return PrivacySettingsRepositoryImpl(db.privacySettingsDao())
    }

    @Provides
    @Singleton
    fun provideGetConnectionStatusUseCase(connectionsRepository: ConnectionsRepository): GetConnectionStatusUseCase {
        return GetConnectionStatusUseCaseImpl(connectionsRepository)
    }

    @Provides
    @Singleton
    fun provideGetSettingsStatusUseCase(settingsRepository: PrivacySettingsRepository): GetSettingsUseCase {
        return GetSettingsUseCaseImpl(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserAlbumCountUseCase(albumRepository: AlbumRepository): GetUserAlbumCountUseCase {
        return GetUserAlbumCountUseCaseImpl(albumRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserDataUseCase(userRepository: UserRepository): GetUserDataUseCase {
        return GetUserDataUseCasesImpl(userRepository)
    }

    @Provides
    @Singleton
    fun shortlistsUseCases(getShortlistedProfilesUseCase: GetShortlistedProfilesUseCaseImpl, removeShortlistUseCase: RemoveShortlistUseCaseImpl): ShortlistsUseCases {
        return ShortlistsUseCases(getShortlistedProfilesUseCase,removeShortlistUseCase)
    }

    @Provides
    @Singleton
    fun provideGetShortlistedProfilesUseCase(shortlistsRepository: ShortlistsRepository): GetShortlistedProfilesUseCase {
        return GetShortlistedProfilesUseCaseImpl(shortlistsRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveShortlistUseCase(shortlistsRepository: ShortlistsRepository): RemoveShortlistUseCase {
        return RemoveShortlistUseCaseImpl(shortlistsRepository)
    }
}
package com.example.matrimony.di

import android.app.Application
import androidx.room.Room
import com.example.matrimony.db.AppDatabase
import com.example.matrimony.db.repository.*
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
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "app_database").build()
    }


    @Provides
    @Singleton
    fun provideUserRepository(db: AppDatabase): UserRepository {
        return UserRepository(db.userDao(),db.accountDao())
    }

    @Provides
    @Singleton
    fun provideLoginRepository(db: AppDatabase): LoginRepository {
        return LoginRepository(db.accountDao())
    }

    @Provides
    @Singleton
    fun provideInjectDataRepository(db: AppDatabase): InjectDataRepository {
        return InjectDataRepository(db.accountDao(), db.userDao())
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(db: AppDatabase): AccountRepository {
        return AccountRepository(db.accountDao(), db.userDao())
    }

    @Provides
    @Singleton
    fun provideConnectionsRepository(db: AppDatabase): ConnectionsRepository {
        return ConnectionsRepository(db.connectionsDao(), db.accountDao(), db.userDao())
    }

    @Provides
    @Singleton
    fun provideShortlistsRepository(db: AppDatabase): ShortlistsRepository {
        return ShortlistsRepository(db.userDao(), db.shortlistsDao())
    }

    @Provides
    @Singleton
    fun provideHabitsRepository(db: AppDatabase): HabitsRepository {
        return HabitsRepository(db.habitsDao())
    }

    @Provides
    @Singleton
    fun provideHobbiesRepository(db: AppDatabase): HobbiesRepository {
        return HobbiesRepository(db.hobbiesDao())
    }

    @Provides
    @Singleton
    fun provideFamilyDetailsDao(db: AppDatabase): FamilyDetailsRepository {
        return FamilyDetailsRepository(db.familyDetailsDao())
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(db: AppDatabase): AlbumRepository {
        return AlbumRepository(db.albumDao())
    }

    @Provides
    @Singleton
    fun providePartnerPreferenceRepository(db: AppDatabase): PartnerPreferenceRepository {
        return PartnerPreferenceRepository(db.partnerPreferenceDao())
    }

    @Provides
    @Singleton
    fun provideMeetingsRepository(db: AppDatabase): MeetingsRepository {
        return MeetingsRepository(db.meetingsDao())
    }

    @Provides
    @Singleton
    fun providePrivacySettingsRepository(db: AppDatabase): PrivacySettingsRepository {
        return PrivacySettingsRepository(db.privacySettingsDao())
    }


}
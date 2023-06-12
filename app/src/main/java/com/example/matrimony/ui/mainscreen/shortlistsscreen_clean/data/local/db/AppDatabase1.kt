package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.matrimony.db.converters.BitmapConverter
import com.example.matrimony.db.converters.DateConverter
import com.example.matrimony.db.converters.ListConverter
import com.example.matrimony.db.dao.*
import com.example.matrimony.db.entities.*
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.dao.AlbumDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.dao.ConnectionsDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.dao.PrivacySettingsDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.dao.ShortlistsDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.dao.UserDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model.Album
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model.Connections
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model.PrivacySettings
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model.Shortlists
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model.User


//@Database(
//    entities = [
//        Account::class,
//        Connections::class,
//        FamilyDetails::class,
//        Habits::class,
//        Hobbies::class,
//        Meetings::class,
//        Album::class,
//        PartnerPreferences::class,
//        PrivacySettings::class,
//        Shortlists::class,
//        User::class],
//    version = 1,
//    exportSchema = false
//)
//@TypeConverters(BitmapConverter::class, DateConverter::class, ListConverter::class)
abstract class AppDatabase1 : RoomDatabase() {



    abstract fun accountDao(): AccountDao
    abstract fun connectionsDao(): ConnectionsDao
    abstract fun hobbiesDao(): HobbiesDao
    abstract fun meetingsDao(): MeetingsDao
    abstract fun albumDao(): AlbumDao
    abstract fun partnerPreferenceDao(): PartnerPreferenceDao
    abstract fun privacySettingsDao(): PrivacySettingsDao
    abstract fun shortlistsDao(): ShortlistsDao
    abstract fun userDao(): UserDao
    abstract fun habitsDao(): HabitsDao
    abstract fun familyDetailsDao(): FamilyDetailsDao


}
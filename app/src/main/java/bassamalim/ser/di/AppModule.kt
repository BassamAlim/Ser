package bassamalim.ser.di

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import androidx.room.Room
import bassamalim.ser.data.database.AppDatabase
import bassamalim.ser.repository.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // Sets how long does the dependencies live
object AppModule {

    @Provides @Singleton  // Sets how many instances of this dependency can be created
    fun provideApplicationContext(application: Application) =
        application.applicationContext!!

    @Provides @Singleton
    fun provideResources(application: Application): Resources =
        application.resources

    @Provides @Singleton
    fun providePreferences(application: Application) =
        PreferenceManager.getDefaultSharedPreferences(application)!!

    @Provides @Singleton
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(
            application, AppDatabase::class.java, "SerDB"
        ).createFromAsset("databases/SerDB.db")
            .allowMainThreadQueries()
            .build()

    @Provides @Singleton
    fun provideRemoteConfig() = FirebaseRemoteConfig.getInstance()

    @Provides @Singleton
    fun provideGson() = Gson()


    @Provides @Singleton
    fun provideAboutRepository(
        pref: SharedPreferences
    ) = AboutRepo(pref)

    @Provides @Singleton
    fun provideAESRepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase,
        gson: Gson
    ) = AESRepo(sharedPreferences, database, gson)

    @Provides @Singleton
    fun provideHomeRepository(
        resources: Resources,
        preferences: SharedPreferences
    ) = HomeRepo(resources, preferences)

    @Provides @Singleton
    fun provideKeysRepository(
        database: AppDatabase,
        gson: Gson
    ) = KeysRepo(database, gson)

    @Provides @Singleton
    fun provideMainRepository(
        res: Resources,
        sp: SharedPreferences
    ) = MainRepo(res, sp)

    @Provides @Singleton
    fun provideRSARepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase,
        gson: Gson
    ) = RSARepo(sharedPreferences, database, gson)

    @Provides @Singleton
    fun provideSettingsRepository(
        resources: Resources,
        preferences: SharedPreferences
    ) = SettingsRepo(resources, preferences)

    @Provides @Singleton
    fun provideWelcomeRepository(
        preferences: SharedPreferences
    ) = WelcomeRepo(preferences)

}
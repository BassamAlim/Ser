package bassamalim.ser.core.di

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import androidx.room.Room
import bassamalim.ser.core.data.database.AppDatabase
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.features.about.AboutRepo
import bassamalim.ser.features.aes.AESRepo
import bassamalim.ser.features.aesKeyGen.AESKeyGenRepo
import bassamalim.ser.features.home.HomeRepo
import bassamalim.ser.features.keyPicker.KeyPickerRepo
import bassamalim.ser.features.keyPublisher.KeyPublisherRepo
import bassamalim.ser.features.keyRename.KeyRenameRepo
import bassamalim.ser.features.keyStore.KeyStoreRepo
import bassamalim.ser.features.keys.KeysRepo
import bassamalim.ser.features.main.MainRepo
import bassamalim.ser.features.rsa.RSARepo
import bassamalim.ser.features.rsaKeyGen.RSAKeyGenRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    fun provideFirestore() = Firebase.firestore

    @Provides @Singleton
    fun provideGson() = Gson()

    @Provides @Singleton
    fun provideKeyKeeper(
        database: AppDatabase,
        gson: Gson
    ) = KeyKeeper(database, gson)


    @Provides @Singleton
    fun provideAboutRepository(
        pref: SharedPreferences
    ) = AboutRepo(pref)

    @Provides @Singleton
    fun provideAESRepository(
        keyKeeper: KeyKeeper
    ) = AESRepo(keyKeeper)

    @Provides @Singleton
    fun provideHomeRepository(
        resources: Resources,
        preferences: SharedPreferences
    ) = HomeRepo(resources, preferences)

    @Provides @Singleton
    fun provideKeysRepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase,
        keyKeeper: KeyKeeper
    ) = KeysRepo(sharedPreferences, database, keyKeeper)

    @Provides @Singleton
    fun provideKeyStoreRepository(
        sharedPreferences: SharedPreferences,
        firestore: FirebaseFirestore
    ) = KeyStoreRepo(sharedPreferences, firestore)

    @Provides @Singleton
    fun provideMainRepository(
        res: Resources,
        sp: SharedPreferences
    ) = MainRepo(res, sp)

    @Provides @Singleton
    fun provideRSARepository(
        keyKeeper: KeyKeeper
    ) = RSARepo(keyKeeper)

    @Provides @Singleton
    fun provideAESKeyGenRepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase,
        keyKeeper: KeyKeeper
    ) = AESKeyGenRepo(sharedPreferences, database, keyKeeper)

    @Provides @Singleton
    fun provideRSAKeyGenRepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase,
        keyKeeper: KeyKeeper
    ) = RSAKeyGenRepo(sharedPreferences, database, keyKeeper)

    @Provides @Singleton
    fun provideKeyPickerRepository(
        sharedPreferences: SharedPreferences,
        keyKeeper: KeyKeeper
    ) = KeyPickerRepo(sharedPreferences, keyKeeper)

    @Provides @Singleton
    fun provideKeyPublisherRepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase,
        firestore: FirebaseFirestore,
        keyKeeper: KeyKeeper
    ) = KeyPublisherRepo(sharedPreferences, database, firestore, keyKeeper)

    @Provides @Singleton
    fun provideKeyRenameRepository(
        sharedPreferences: SharedPreferences,
        database: AppDatabase
    ) = KeyRenameRepo(sharedPreferences, database)

}
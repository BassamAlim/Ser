package bassamalim.ser

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import bassamalim.ser.data.GlobalVals
import bassamalim.ser.data.Prefs
import bassamalim.ser.enums.Algorithm
import bassamalim.ser.enums.Language
import bassamalim.ser.helpers.Cryptography
import bassamalim.ser.helpers.KeyKeeper
import bassamalim.ser.nav.Navigator
import bassamalim.ser.ui.theme.AppTheme
import bassamalim.ser.utils.ActivityUtils
import bassamalim.ser.utils.DBUtils
import bassamalim.ser.utils.PrefUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var sp: SharedPreferences
//    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        handleAction(intent.action)

        preLaunch()

        launch()

        postLaunch()
    }

    private fun init() {
        sp = PrefUtils.getPreferences(this)
//        db = DBUtils.getDB(this)
    }

    private fun handleAction(action: String?) {
        if (action == null) return

        /*when (action) {
            Global.### -> {

            }
        }*/
    }

    private fun preLaunch() {
        DBUtils.testDB(this, sp)

//        ActivityUtils.onActivityCreateSetLocale(this)
        ActivityUtils.onActivityCreateSetTheme(this)  // a must for the app to use xml themes
//        ActivityUtils.onActivityCreateSetLocale(applicationContext)
        ActivityUtils.onActivityCreateSetTheme(applicationContext)

        val shouldWelcome = PrefUtils.getBoolean(sp, Prefs.FirstTime)
        if (shouldWelcome) welcome()
    }

    private fun launch() {
//        val shouldWelcome = PrefUtils.getBoolean(sp, Prefs.FirstTime)
        val navRoute = intent.getStringExtra("start_route")

        setContent {
            ActivityUtils.onActivityCreateSetLocale(LocalContext.current)

            AppTheme(
                direction = getDirection()
            ) {
                Navigator(navRoute, false)
            }
        }
    }

    private fun postLaunch() {
        initFirebase()

        fetchAndActivateRemoteConfig()

//        setupBootReceiver()
    }

    private fun getDirection(): LayoutDirection {
        val language = PrefUtils.getLanguage(sp)

        return if (language == Language.ENGLISH) LayoutDirection.Ltr
        else LayoutDirection.Rtl
    }

    /**
     * It generates an AES key and an RSA key pair, and stores them in the database
     */
    private fun welcome() {
        val db = DBUtils.getDB(this)
        val gson = Gson()

        // generate AES key
        val key = Cryptography.generateAESKey()
        // store AES key
        val aesKeyKeeper = KeyKeeper(db, gson, Algorithm.AES)
        aesKeyKeeper.store("Default AES key", key)

        // generate RSA key pair
        val keyPair = Cryptography.generateRSAKey()
        // store RSA key pair
        val rsaKeyKeeper = KeyKeeper(db, gson, Algorithm.RSA)
        rsaKeyKeeper.store("Default RSA key pair", keyPair)

        // set first time to false
        sp.edit()
            .putBoolean(Prefs.FirstTime.key, false)
            .apply()
    }

    private fun initFirebase() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            // update at most every six hours
            .setMinimumFetchIntervalInSeconds(3600 * 6).build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private fun fetchAndActivateRemoteConfig() {
        FirebaseRemoteConfig.getInstance().fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) Log.i(GlobalVals.TAG, "RemoteConfig update Success")
                else Log.e(GlobalVals.TAG, "RemoteConfig update Failed")
            }
    }

    /*private fun setupBootReceiver() {
        packageManager.setComponentEnabledSetting(
            ComponentName(this, DeviceBootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }*/

}


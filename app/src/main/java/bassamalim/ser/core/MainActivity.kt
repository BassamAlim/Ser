package bassamalim.ser.core

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import bassamalim.ser.R
import bassamalim.ser.core.data.GlobalVals
import bassamalim.ser.core.data.Prefs
import bassamalim.ser.core.helpers.Cryptography
import bassamalim.ser.core.helpers.KeyKeeper
import bassamalim.ser.core.models.AESKey
import bassamalim.ser.core.models.RSAKeyPair
import bassamalim.ser.core.nav.Navigator
import bassamalim.ser.core.ui.theme.AppTheme
import bassamalim.ser.core.utils.DBUtils
import bassamalim.ser.core.utils.PrefUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = PrefUtils.getPreferences(this)

        preLaunch()

        launch()

        postLaunch()
    }


    private fun preLaunch() {
        DBUtils.testDB(this, sp)

        val shouldWelcome = PrefUtils.getBoolean(sp, Prefs.FirstTime)
        if (shouldWelcome) welcome()
    }

    private fun launch() {
        val navRoute = intent.getStringExtra("start_route")

        setContent {
            AppTheme {
                Navigator(navRoute)
            }
        }
    }

    private fun postLaunch() {
        initFirebase()

        fetchAndActivateRemoteConfig()
    }

    /**
     * It generates an AES key and an RSA key pair, and stores them in the database
     */
    private fun welcome() {
        val keyKeeper = KeyKeeper(
            DBUtils.getDB(this),
            Gson()
        )

        // generate AES key
        val key = Cryptography.generateAESKey()
        // store AES key
        keyKeeper.storeAESKey(
            AESKey("Default AES key", key)
        )

        // generate RSA key pair
        val keyPair = Cryptography.generateRSAKey()
        // store RSA key pair
        keyKeeper.storeRSAKey(
            RSAKeyPair("Default RSA key pair", keyPair)
        )

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

}
package bassamalim.ser

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import bassamalim.ser.data.Prefs
import bassamalim.ser.enums.Language
import bassamalim.ser.nav.Navigator
import bassamalim.ser.data.GlobalVals
import bassamalim.ser.ui.theme.AppTheme
import bassamalim.ser.utils.ActivityUtils
import bassamalim.ser.utils.PrefUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
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
//        DBUtils.testDB(this, sp)

//        ActivityUtils.onActivityCreateSetLocale(this)
        ActivityUtils.onActivityCreateSetTheme(this)
//        ActivityUtils.onActivityCreateSetLocale(applicationContext)
        ActivityUtils.onActivityCreateSetTheme(applicationContext)
    }

    private fun launch() {
        val shouldWelcome = PrefUtils.getBoolean(sp, Prefs.FirstTime)
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

    private fun getDirection(): LayoutDirection {
        val language = PrefUtils.getLanguage(sp)

        return if (language == Language.ENGLISH) LayoutDirection.Ltr
        else LayoutDirection.Rtl
    }

    private fun postLaunch() {
        initFirebase()

        fetchAndActivateRemoteConfig()

//        setupBootReceiver()
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


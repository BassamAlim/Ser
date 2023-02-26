package bassamalim.ser.view

import android.app.Activity
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bassamalim.ser.R
import bassamalim.ser.data.Prefs
import bassamalim.ser.enums.Language
import bassamalim.ser.enums.Theme
import bassamalim.ser.ui.components.ExpandableCard
import bassamalim.ser.ui.components.ListPref
import bassamalim.ser.ui.components.MyFatColumn
import bassamalim.ser.ui.components.MyScaffold
import bassamalim.ser.utils.ActivityUtils
import bassamalim.ser.viewmodel.SettingsVM

@Composable
fun SettingsUI(
    vm: SettingsVM = hiltViewModel()
) {
    val st by vm.uiState.collectAsState()
    val activity = LocalContext.current as Activity

    MyScaffold(
        stringResource(R.string.settings)
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            MyFatColumn {
                ExpandableCard(
                    R.string.appearance,
                    Modifier.padding(top = 4.dp, bottom = 2.dp)
                ) {
                    AppearanceSettings(activity, vm.sp)
                }
            }
        }
    }
}

@Composable
fun AppearanceSettings(activity: Activity, pref: SharedPreferences) {
    Column(
        Modifier.padding(bottom = 10.dp)
    ) {
        // Language
        ListPref(
            sp = pref,
            titleResId = R.string.language,
            pref = Prefs.Language,
            iconResId = R.drawable.ic_translation,
            entries = stringArrayResource(R.array.language_entries),
            values = Language.values().map { it.name }.toTypedArray()
        ) {
            ActivityUtils.restartActivity(activity)
        }

        // Numerals language
        ListPref(
            sp = pref,
            titleResId = R.string.numerals_language,
            pref = Prefs.NumeralsLanguage,
            iconResId = R.drawable.ic_translation,
            entries = stringArrayResource(R.array.numerals_language_entries),
            values = Language.values().map { it.name }.toTypedArray()
        ) {
            ActivityUtils.restartActivity(activity)
        }

        // Theme
        ListPref(
            sp = pref,
            titleResId = R.string.theme,
            pref = Prefs.Theme,
            iconResId = R.drawable.ic_theme,
            entries = stringArrayResource(R.array.themes_entries),
            values = Theme.values().map { it.name }.toTypedArray()
        ) {
            ActivityUtils.restartActivity(activity)
        }
    }
}
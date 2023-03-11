package bassamalim.ser.core.data

import bassamalim.ser.core.enums.Language.ENGLISH
import bassamalim.ser.core.enums.Theme.LIGHT

sealed class Prefs(val key: String, val default: Any) {
    object Language : Prefs("language_key", ENGLISH.name)
    object NumeralsLanguage : Prefs("numerals_language_key", ENGLISH.name)
    object Theme : Prefs("theme_key", LIGHT.name)
    object FirstTime: Prefs("new_user", true)
    object SelectedAESKey: Prefs("selected_aes_key", "Default AES key")
    object SelectedRSAKey: Prefs("selected_rsa_key", "Default RSA key pair")
    object LastDBVersion: Prefs("last_db_version", 1)

    /*data class ####(val ##: ###): Prefs(
        key = "${##}##",
        default = 0
    )*/
}
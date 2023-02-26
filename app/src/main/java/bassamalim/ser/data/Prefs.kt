package bassamalim.ser.data

import bassamalim.ser.enums.Language.ENGLISH
import bassamalim.ser.enums.Theme.LIGHT

sealed class Prefs(val key: String, val default: Any) {
    object Language : Prefs("language_key", ENGLISH.name)
    object NumeralsLanguage : Prefs("numerals_language_key", ENGLISH.name)
    object Theme : Prefs("theme_key", LIGHT.name)
    object FirstTime: Prefs("new_user", true)
    object AESKey: Prefs("aes_key", "")
    object RSAKey: Prefs("rsa_key", "")
    object LastDBVersion: Prefs("last_db_version", 1)

    /*data class ####(val ##: ###): Prefs(
        key = "${##}##",
        default = 0
    )*/
}
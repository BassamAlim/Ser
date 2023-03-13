package bassamalim.ser.core.data

sealed class Prefs(val key: String, val default: Any) {
    object FirstTime: Prefs("new_user", true)
    object SelectedAESKeyName: Prefs("selected_aes_key_name", "Default AES key")
    object SelectedRSAKeyName: Prefs("selected_rsa_key_name", "Default RSA key pair")
    object LastDBVersion: Prefs("last_db_version", 1)
    object PublishedKeyValue: Prefs("published_key_value", "")

    /*data class ####(val ##: ###): Prefs(
        key = "${##}##",
        default = 0
    )*/
}
package bassamalim.ser.utils

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Base64
import android.widget.Toast
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey

object Utils {

    fun encode(bytes: ByteArray) = Base64.encodeToString(bytes, Base64.DEFAULT).trim()

    fun encode(secretKey: SecretKey) = encode(secretKey.encoded)

    fun encode(publicKey: PublicKey) = encode(publicKey.encoded)

    fun encode(privateKey: PrivateKey) = encode(privateKey.encoded)

    fun decode(base64: String): ByteArray = Base64.decode(base64, Base64.DEFAULT)

    fun copyToClipboard(
        app: Application,
        text: String,
        label: String = "Text"
    ) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(
            app,
            "$label copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

}
package bassamalim.ser.utils

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Base64
import android.widget.Toast
import bassamalim.ser.models.MyByteKeyPair
import bassamalim.ser.models.MyKeyPair
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object Utils {

    fun toStore(keyPair: MyKeyPair): MyByteKeyPair {
        val publicKey = keyPair.public
        val privateKey = keyPair.private

        return MyByteKeyPair(
            public = publicKey.encoded,
            private = privateKey.encoded
        )
    }

    fun fromStore(keyPair: MyByteKeyPair): MyKeyPair {
        val publicKey = keyPair.public
        val privateKey = keyPair.private

        val keyFactory = KeyFactory.getInstance("RSA")

        val publicKeyObj = keyFactory.generatePublic(X509EncodedKeySpec(publicKey))
        val privateKeyObj = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKey))

        return MyKeyPair(
            publicKeyObj,
            privateKeyObj
        )
    }

    fun toStore(key: SecretKey): ByteArray = key.encoded

    fun fromStore(key: ByteArray): SecretKey = SecretKeySpec(key, "AES")

    fun encode(bytes: ByteArray) = Base64.encodeToString(bytes, Base64.DEFAULT).trim()

    fun encode(secretKey: SecretKey) = encode(secretKey.encoded)

    fun encode(key: Key) = encode(key.encoded)

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
package bassamalim.ser.core.utils

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import bassamalim.ser.core.models.MyByteKeyPair
import bassamalim.ser.core.models.MyKeyPair
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object Converter {

    fun encode(bytes: ByteArray): String =
        Base64.encodeToString(bytes, Base64.DEFAULT).trim()

    fun decode(base64: String): ByteArray = Base64.decode(base64, Base64.DEFAULT)

    fun toSecretKey(key: ByteArray): SecretKey = SecretKeySpec(key, "AES")

    fun toSecretKey(key: String): SecretKey = SecretKeySpec(decode(key), "AES")

    private fun toPublicKey(
        key: ByteArray,
        keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    ): PublicKey {
        return keyFactory.generatePublic(X509EncodedKeySpec(key))
    }

    fun toPublicKey(
        key: String,
        keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    ): PublicKey {
        return toPublicKey(decode(key), keyFactory)
    }

    private fun toPrivateKey(
        key: ByteArray,
        keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    ): PrivateKey {
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(key))
    }

    fun toPrivateKey(
        key: String,
        keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    ): PrivateKey {
        return toPrivateKey(decode(key), keyFactory)
    }

    fun convert(keyPair: MyKeyPair): MyByteKeyPair {
        return MyByteKeyPair(
            public = keyPair.public.encoded,
            private = keyPair.private.encoded
        )
    }

    fun convert(keyPair: MyByteKeyPair): MyKeyPair {
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeyObj = toPublicKey(keyPair.public, keyFactory)
        val privateKeyObj = toPrivateKey(keyPair.private, keyFactory)

        return MyKeyPair(
            publicKeyObj,
            privateKeyObj
        )
    }

    fun uriToByteArray(uri: Uri, contentResolver: ContentResolver): ByteArray? {
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return bytes
    }

}
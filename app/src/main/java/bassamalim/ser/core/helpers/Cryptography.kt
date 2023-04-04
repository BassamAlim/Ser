package bassamalim.ser.core.helpers

import android.util.Log
import bassamalim.ser.core.models.MyKeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object Cryptography {

    fun generateAESKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256, SecureRandom())
        return keyGen.generateKey()
    }

    private fun generateAESIV(): ByteArray {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return iv
    }

    fun encryptAES(
        bytes: ByteArray,
        secretKey: SecretKey
    ): ByteArray? {
        val ivSpec = IvParameterSpec(generateAESIV())

        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val ciphertext = cipher.doFinal(bytes)

            val cipherArr = ByteArray(ivSpec.iv.size + ciphertext.size)
            System.arraycopy(ivSpec.iv, 0, cipherArr, 0, ivSpec.iv.size)
            System.arraycopy(ciphertext, 0, cipherArr, ivSpec.iv.size, ciphertext.size)

            cipherArr
        } catch (e: Exception) {
            Log.e("Cryptography", e.toString())
            null
        }
    }

    fun decryptAES(
        bytes: ByteArray,
        secretKey: SecretKey
    ): ByteArray? {
        return try {
            val iv = bytes.copyOfRange(0, 16)
            val decodedCT = bytes.copyOfRange(16, bytes.size)
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

            cipher.doFinal(decodedCT)
        } catch (e: Exception) {
            Log.e("Cryptography", e.toString())
            null
        }
    }


    fun generateRSAKey(): MyKeyPair {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048, SecureRandom())
        val keyPair = kpg.genKeyPair()
        return MyKeyPair(
            public = keyPair.public,
            private = keyPair.private
        )
    }

    fun encryptRSA(
        bytes: ByteArray,
        publicKey: PublicKey
    ): ByteArray? {
        return try {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            cipher.doFinal(bytes)
        } catch (e: Exception) {
            Log.e("Cryptography", e.toString())
            null
        }
    }

    fun decryptRSA(
        bytes: ByteArray,
        privateKey: PrivateKey
    ): ByteArray? {
        return try {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            cipher.doFinal(bytes)
        } catch (e: Exception) {
            Log.e("Cryptography", e.toString())
            null
        }
    }

}
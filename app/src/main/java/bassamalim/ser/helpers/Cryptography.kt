package bassamalim.ser.helpers

import bassamalim.ser.models.MyKeyPair
import bassamalim.ser.utils.Converter
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
        return KeyGenerator.getInstance("AES").generateKey()
    }

    private fun generateAESIV(): ByteArray {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return iv
    }

    fun encryptAES(
        plaintext: String,
        secretKey: SecretKey
    ): String {
        val bytes = plaintext.toByteArray()
        val ivSpec = IvParameterSpec(generateAESIV())

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val ciphertext = cipher.doFinal(bytes)

        val cipherArr = ByteArray(ivSpec.iv.size + ciphertext.size)
        System.arraycopy(ivSpec.iv, 0, cipherArr, 0, ivSpec.iv.size)
        System.arraycopy(ciphertext, 0, cipherArr, ivSpec.iv.size, ciphertext.size)

        return Converter.encode(cipherArr)
    }

    fun decryptAES(
        ciphertext: String,
        secretKey: SecretKey
    ): String? {
        return try {
            val decoded = Converter.decode(ciphertext)

            val iv = decoded.copyOfRange(0, 16)
            val decodedCT = decoded.copyOfRange(16, decoded.size)

            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val plaintext = cipher.doFinal(decodedCT)
            plaintext.decodeToString()
        } catch (e: Exception) { null }
    }


    fun generateRSAKey(): MyKeyPair {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(256, SecureRandom())
        val keyPair = kpg.genKeyPair()
        return MyKeyPair(
            public = keyPair.public,
            private = keyPair.private
        )
    }

    fun encryptRSA(
        plaintext: String,
        publicKey: PublicKey
    ): String {
        val bytes = plaintext.toByteArray()

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val ciphertext = cipher.doFinal(bytes)

        return Converter.encode(ciphertext)
    }

    fun decryptRSA(
        ciphertext: String,
        privateKey: PrivateKey
    ): String? {
        return try {
            val decoded = Converter.decode(ciphertext)

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)

            cipher.doFinal(decoded).decodeToString()
        } catch (e: Exception) { null }
    }

}
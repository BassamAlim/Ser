package bassamalim.ser.helpers

import bassamalim.ser.utils.Utils
import java.security.*
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

        return Utils.encode(cipherArr)
    }

    fun decryptAES(
        ciphertext: String,
        secretKey: SecretKey
    ): String {
        val decodedCiphertext = Utils.decode(ciphertext)

        val iv = decodedCiphertext.copyOfRange(0, 16)
        val decodedCT = decodedCiphertext.copyOfRange(16, decodedCiphertext.size)

        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val plaintext = cipher.doFinal(decodedCT)
        return plaintext.decodeToString()
    }


    fun generateRSAKey(): KeyPair {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048, SecureRandom())
        return kpg.genKeyPair()
    }

    fun encryptRSA(
        plaintext: String,
        publicKey: PublicKey
    ): String {
        val bytes = plaintext.toByteArray()

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val ciphertext = cipher.doFinal(bytes)

        return Utils.encode(ciphertext)
    }

    fun decryptRSA(
        ciphertext: String,
        privateKey: PrivateKey
    ): String {
        val decodedCiphertext = Utils.decode(ciphertext)

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val plaintext = cipher.doFinal(decodedCiphertext)

        return plaintext.decodeToString()
    }

}
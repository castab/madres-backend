package madres.encryption

import com.fasterxml.jackson.module.kotlin.readValue
import madres.common.objectMapper
import madres.inquiry.Inquiry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class EncryptionService(
    @param:Value($$"${madres.encryptionKey}")
    private val keyString: String
) {
    private val key = keyString.toByteArray(Charsets.UTF_8)
    companion object {
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
    }

    private val secureRandom = SecureRandom()

    fun encrypt(inquiryData: Inquiry.Data): ByteArray {
        val json = objectMapper.writeValueAsBytes(inquiryData)

        val iv = ByteArray(GCM_IV_LENGTH)
        secureRandom.nextBytes(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        val cipherText = cipher.doFinal(json)

        return iv + cipherText // prepend IV
    }

    fun decrypt(data: ByteArray): Inquiry.Data {
        val iv = data.copyOfRange(0, GCM_IV_LENGTH)
        val cipherText = data.copyOfRange(GCM_IV_LENGTH, data.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        val plainBytes = cipher.doFinal(cipherText)

        return objectMapper.readValue(plainBytes)
    }
}

package madres.inquiry

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.github.oshai.kotlinlogging.KotlinLogging
import madres.encryption.EncryptionService
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.time.ZoneId
import java.util.UUID

@Repository
class InquiryRepository(
    private val jdbi: Jdbi,
    private val encryptionService: EncryptionService
) {
    private val log = KotlinLogging.logger{}

    fun addNewInquiry(data: Inquiry.Data, zoneId: ZoneId = ZoneId.of("UTC")): Result<UUID, Throwable> = try {
        val id = jdbi.withHandle<UUID, Exception> { handle ->
            handle.createQuery("""
                INSERT INTO madres.inquiry (id, zone_id, data) 
                VALUES (:id, :zoneId, :data) RETURNING id;
            """.trimIndent())
                .bind("id", UUID.randomUUID())
                .bind("zoneId", zoneId)
                .bind("data", encryptionService.encrypt(data))
                .mapTo(UUID::class.java)
                .one()
        }
        log.debug { "Inserted inquiry for $id" }
        Ok(id)
    } catch (e: Exception) {
        log.error { "Error writing new inquiry" }
        Err(e)
    }

    fun getExistingInquiryById(id: UUID): Result<Inquiry.Existing?, Exception> = try {
        val dto = jdbi.withHandle<Inquiry.DTO?, Exception> { handle ->
            handle.createQuery("""
                SELECT * FROM madres.inquiry WHERE id = :id;
            """.trimIndent())
                .bind("id", id)
                .map(Inquiry.DTO.rowMapper)
                .firstOrNull()
        }
        Ok(dto?.toExisting())
    } catch (e: Exception) {
        log.error(e) { "There was a problem retrieving $id" }
        Err(e)
    }

    fun getPendingReplyInquiries(): Result<List<Inquiry.Existing>, Exception> = try {
        val dtos = jdbi.withHandle<List<Inquiry.DTO>, Exception> { handle ->
            handle.createQuery("""
                SELECT * FROM madres.inquiry WHERE replied_to = false ORDER BY updated_at;
            """.trimIndent())
                .map(Inquiry.DTO.rowMapper)
                .toList()
        }
        Ok(dtos.map { it.toExisting() })
    } catch (e: Exception) {
        log.error(e) { "There was a problem retrieving pending inquiries" }
        Err(e)
    }

    fun countPendingConfirmations(): Result<Int, Throwable> = try {
        val count = jdbi.withHandle<Int, Exception> { handle ->
            handle.createQuery("""
                SELECT COUNT(*) FROM madres.inquiry 
                WHERE confirmation_processed = false;
            """.trimIndent())
                .mapTo(Int::class.java)
                .one()
        }
        Ok(count)
    } catch (e: Exception) {
        log.error(e) { "There was a problem retrieving the count for confirmations to send" }
        Err(e)
    }

    fun usingTransactionHandle(block: (Handle) -> Unit) = jdbi.useTransaction<Exception> { block(it) }
    fun getOnePendingConfirmationInquiry(handle: Handle): Inquiry.Existing? = handle.createQuery("""
            SELECT * FROM madres.inquiry 
            WHERE confirmation_processed = false 
            ORDER BY updated_at 
            LIMIT 1 
            FOR UPDATE SKIP LOCKED;
        """.trimIndent())
            .map(Inquiry.DTO.rowMapper)
            .firstOrNull()
            ?.toExisting()
    fun markConfirmationSent(id: UUID, handle: Handle): Int = handle.createUpdate("""
            UPDATE madres.inquiry
            SET confirmation_processed = true,
                confirmation_processed_at = NOW(),
                updated_at = NOW()
            WHERE id = :id;
        """.trimIndent())
        .bind("id", id)
        .execute()

    private fun Inquiry.DTO.toExisting() = Inquiry.Existing(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        zoneId = zoneId,
        repliedTo = repliedTo,
        confirmationProcessed = confirmationProcessed,
        repliedAt = repliedAt,
        confirmationProcessedAt = confirmationProcessedAt,
        inquiryData = encryptionService.decrypt(inquiryDataBytes)
    )
}

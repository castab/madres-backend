package madres.inquiry

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.jdbi.v3.core.mapper.RowMapper
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

sealed class Inquiry{
    data class Existing(
        val id: UUID,
        val createdAt: Instant,
        val updatedAt: Instant,
        val zoneId: ZoneId,
        val repliedTo: Boolean,
        val confirmationProcessed: Boolean,
        val repliedAt: Instant?,
        val confirmationProcessedAt: Instant?,
        val inquiryData: Data
    ): Inquiry()

    data class DTO(
        val id: UUID,
        val createdAt: Instant,
        val updatedAt: Instant,
        val zoneId: ZoneId,
        val repliedTo: Boolean,
        val confirmationProcessed: Boolean,
        val repliedAt: Instant?,
        val confirmationProcessedAt: Instant?,
        val inquiryDataBytes: ByteArray
    ): Inquiry() {
        companion object {
            val rowMapper = RowMapper<DTO> { rs, ctx ->
                DTO(
                    id = UUID.fromString(rs.getString("id")),
                    createdAt = rs.getTimestamp("created_at").toInstant(),
                    updatedAt = rs.getTimestamp("updated_at").toInstant(),
                    zoneId = ZoneId.of(rs.getString("zone_id")),
                    repliedTo = rs.getBoolean("replied_to"),
                    confirmationProcessed = rs.getBoolean("confirmation_processed"),
                    repliedAt = rs.getTimestamp("replied_at")?.toInstant(),
                    confirmationProcessedAt = rs.getTimestamp("confirmation_processed_at")?.toInstant(),
                    inquiryDataBytes = rs.getBytes("data")
                )
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DTO

            if (repliedTo != other.repliedTo) return false
            if (confirmationProcessed != other.confirmationProcessed) return false
            if (id != other.id) return false
            if (createdAt != other.createdAt) return false
            if (updatedAt != other.updatedAt) return false
            if (zoneId != other.zoneId) return false
            if (repliedAt != other.repliedAt) return false
            if (confirmationProcessedAt != other.confirmationProcessedAt) return false
            if (!inquiryDataBytes.contentEquals(other.inquiryDataBytes)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = repliedTo.hashCode()
            result = 31 * result + confirmationProcessed.hashCode()
            result = 31 * result + id.hashCode()
            result = 31 * result + createdAt.hashCode()
            result = 31 * result + updatedAt.hashCode()
            result = 31 * result + zoneId.hashCode()
            result = 31 * result + repliedAt.hashCode()
            result = 31 * result + confirmationProcessedAt.hashCode()
            result = 31 * result + inquiryDataBytes.contentHashCode()
            return result
        }
    }

    data class Data(
        val name: String,
        val phone: String? = null,
        val email: String? = null,
        val address: String? = null,
        val comments: String? = null,
    ): Inquiry() {
        fun validate(): Result<Data, IllegalArgumentException> = try {
            require(name.isNotBlank()) { "A name must be provided" }
            require(!phone.isNullOrBlank() || !email.isNullOrBlank()) {
                "At least one of phone or email must be provided"
            }
            Ok(this)
        } catch (e: IllegalArgumentException) {
            Err(e)
        }
    }
}
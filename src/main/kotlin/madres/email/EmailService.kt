package madres.email

import com.resend.Resend
import com.resend.services.emails.model.CreateEmailOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import madres.inquiry.Inquiry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EmailService(
    @param:Value($$"${madres.emailApiKey}")
    private val apiKey: String
) {
    private val log = KotlinLogging.logger{}
    private val resend = Resend(apiKey)

    fun sendConfirmation(existing: Inquiry.Existing): String? {
        return if (existing.inquiryData.email.isNullOrBlank()) {
            log.debug { "No email listed for inquiry ${existing.id}" }
            null
        } else {
            val params = CreateEmailOptions.builder()
                .from("Madres Taco Shop <inquiry@madrestacoshop.com>")
                .to(existing.inquiryData.email)
                .subject("Thanks for your inquiry!")
                .html("<strong>Hey there, ${existing.inquiryData.name}, thanks for contacting us. We're working on your inquiry and will get back to you soon!</strong>")
                .build()
            resend.emails().send(params).id
        }
    }
}
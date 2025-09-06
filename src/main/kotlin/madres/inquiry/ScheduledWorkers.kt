package madres.inquiry

import com.github.michaelbull.result.fold
import com.github.michaelbull.result.onSuccess
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import madres.email.EmailService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduledWorkers(
    private val inquiryRepository: InquiryRepository,
    private val emailService: EmailService,
) {
    private val log = KotlinLogging.logger{}

    @Scheduled(cron = $$"${workers.inquiry.cron:-}", zone = $$"${workers.inquiry.zone:UTC}")
    fun runPendingReplies() {
        inquiryRepository.getPendingReplyInquiries().onSuccess { pendingInquiries ->
            pendingInquiries
                .ifEmpty {
                    log.debug { "Found no pending inquiries to work on." }
                    return@onSuccess
                }
                .map{
                    it.id
                }.let {
                    log.debug { "Still need to respond to inquiries: $it" }
                }
        }
    }

    @Scheduled(cron = $$"${workers.confirmation.cron:-}", zone = $$"${workers.confirmation.zone:UTC}")
    fun runConfirmations() = runBlocking {
        inquiryRepository.countPendingConfirmations().fold(
            success = { count ->
                if (count == 0) {
                    log.trace { "No currently pending inquiries to send confirmations for have been found." }
                    return@fold // noOp
                }
                // Run only 2 at a time to keep under max 2tps limit
                launch(Dispatchers.IO) { confirmOneInquiry() }
                launch(Dispatchers.IO) { confirmOneInquiry() }
            },
            failure = {
                log.error(it) { "Scheduled run to confirm inquiries failed" }
            }
        )
    }

    fun confirmOneInquiry() = runCatching {
        inquiryRepository.usingTransactionHandle { handle ->
            val inquiry = inquiryRepository.getOnePendingConfirmationInquiry(handle) ?: return@usingTransactionHandle
            log.debug { "Working on confirmation email for inquiry ${inquiry.id}" }
            val emailId = emailService.sendConfirmation(inquiry)
            inquiryRepository.markConfirmationSent(inquiry.id, handle)
            log.debug { "Confirmation email sent and marked for inquiry ${inquiry.id} | Email ID: $emailId" }
        }
    }
        .onFailure { ex ->
            log.error(ex) { "Problem encountered while sending confirmation email." }
        }
}

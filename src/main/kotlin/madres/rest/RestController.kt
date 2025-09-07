package madres.rest

import com.github.michaelbull.result.fold
import madres.inquiry.Inquiry
import madres.inquiry.InquiryRepository
import madres.random.TestNames
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneId
import java.util.UUID

@RestController
@RequestMapping("/inquiries")
class RestController(
    private val inquiryRepository: InquiryRepository,
) {
    @GetMapping("/test")
    fun test(): ResponseEntity<Unit> {
        val data = Inquiry.Data(TestNames.randomFullName())
        return inquiryRepository.addNewInquiry(data, ZoneId.of("America/Los_Angeles"))
            .fold(
                success = { ResponseEntity.status(HttpStatus.ACCEPTED).build() },
                failure = { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() }
            )
    }

    @GetMapping("/{id}")
    fun getInquiry(@PathVariable id: UUID): ResponseEntity<Inquiry.Existing?> {
        return inquiryRepository.getExistingInquiryById(id)
            .fold(
                success = {
                    if (it == null) ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                    else ResponseEntity.ok(it)
                  },
                failure = { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() }
            )
    }
}
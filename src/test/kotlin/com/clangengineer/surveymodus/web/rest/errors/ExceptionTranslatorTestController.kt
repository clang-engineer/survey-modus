package com.clangengineer.surveymodus.web.rest.errors

import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/exception-translator-test")
class ExceptionTranslatorTestController {

    @GetMapping("/concurrency-failure")
    fun concurrencyFailure(): Unit = throw ConcurrencyFailureException("test concurrency failure")

    @PostMapping("/method-argument")
    fun methodArgument(@Valid @RequestBody testDTO: TestDTO) = Unit

    @GetMapping("/missing-servlet-request-part")
    fun missingServletRequestPartException(@RequestPart part: String) = Unit

    @GetMapping("/missing-servlet-request-parameter")
    fun missingServletRequestParameterException(@RequestParam param: String) = Unit

    @GetMapping("/access-denied")
    fun accessdenied(): Unit = throw AccessDeniedException("test access denied!")

    @GetMapping("/unauthorized")
    fun unauthorized(): Unit = throw BadCredentialsException("test authentication failed!")

    @GetMapping("/response-status")
    fun exceptionWithResponseStatus(): Unit = throw TestResponseStatusException()

    @GetMapping("/internal-server-error")
    fun internalServerError(): Unit = throw RuntimeException()

    class TestDTO {
        @field:NotNull
        var test: String? = null
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
    class TestResponseStatusException : RuntimeException()
}

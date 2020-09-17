package rw.bk.loanenginecore.models.exceptions

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

//TODO: Make this class smarter to be able to know that it was a network issue, timeout ,etc...
//This class is used to send back response of an api call to another service which called it.
data class ApiResponseException(val isSuccess: Boolean = false,
                                val is403: Boolean = false,
                                val is404: Boolean = false,
                                val is500: Boolean = false,
                                val isTimeout: Boolean = false) : RuntimeException()

class InternalServerErrorException(message: String) : RuntimeException(message)

class ForbiddenException(message: String) : RuntimeException(message)

class NotFoundException(message: String) : RuntimeException(message)

class UnauthorizedException(message: String) : RuntimeException(message)

/**
 * Exceptions for the API validation
 * For more, ,explore here bro: https://gist.github.com/matsev/4519323
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class ExceptionHandlerBoy {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MissingKotlinParameterException::class)
    fun missingKotlinParameterException(ex: MissingKotlinParameterException): ErrorResponsePayload = createMissingKotlinParameterViolation(ex)

    private fun createMissingKotlinParameterViolation(ex: MissingKotlinParameterException): ErrorResponsePayload =
            ErrorResponsePayload(status = HttpStatus.BAD_REQUEST.value(),
                    error = "BAD_REQUEST",
                    message = "${ex.path[0].fieldName} must not be null",
                    timestamps = Date().toString())


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(ex: ForbiddenException): ErrorResponsePayload =
            ErrorResponsePayload(status = HttpStatus.FORBIDDEN.value(),
                    error = "FORBIDDEN",
                    message = ex.message ?: "Unknown",
                    timestamps = Date().toString())


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ErrorResponsePayload =
            ErrorResponsePayload(status = HttpStatus.UNAUTHORIZED.value(),
                    error = "UNAUTHORIZED",
                    message = ex.message ?: "Unknown",
                    timestamps = Date().toString())


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException::class)
    fun handleUnauthorized(ex: NotFoundException): ErrorResponsePayload =
            ErrorResponsePayload(status = HttpStatus.NOT_FOUND.value(),
                    error = "NOT_FOUND",
                    message = ex.message ?: "Unknown",
                    timestamps = Date().toString())

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalServerError(ex: InternalServerErrorException): ErrorResponsePayload {

        return ErrorResponsePayload(status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "INTERNAL_SERVER_ERROR",
                message = "An error occurred",
                timestamps = Date().toString());
    }


    /**
     * This is used for a bad request payload. Let's say we expect type MALE and FEMALE, you send bonjour
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun badFormatException(ex: HttpMessageNotReadableException): ErrorResponsePayload = createBadFormatException(ex)

    private fun createBadFormatException(ex: HttpMessageNotReadableException): ErrorResponsePayload =
            // Small tweak to display at least the reason of failure.
            ErrorResponsePayload(status = HttpStatus.BAD_REQUEST.value(),
                    error = "BAD_REQUEST",
                    message = ex.toString().take(200),
                    timestamps = Date().toString())


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
            ex: MethodArgumentNotValidException): ErrorResponsePayload =

            ErrorResponsePayload(status = HttpStatus.BAD_REQUEST.value(),
                    error = "BAD_REQUEST",
                    message = (ex.bindingResult.allErrors[0] as FieldError).field + ":" + ex.bindingResult.allErrors[0].defaultMessage,
                    timestamps = Date().toString())

    data class ErrorResponsePayload(val status: Int, val error: String, val message: String, val timestamps: String)
}

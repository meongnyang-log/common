package error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(getHttpStatus(e.errorCode))
            .body(ErrorResponse(e.errorCode))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = e.bindingResult.fieldErrors
            .firstOrNull()?.defaultMessage
            ?: ErrorCode.INVALID_INPUT_VALUE.message

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                code = ErrorCode.INVALID_INPUT_VALUE.code,
                message = message
            )
            )
    }

    private fun getHttpStatus(errorCode: ErrorCode): HttpStatus {
        return when (errorCode) {
            ErrorCode.INVALID_INPUT_VALUE -> HttpStatus.BAD_REQUEST
            ErrorCode.UNAUTHORIZED,
            ErrorCode.INVALID_TOKEN,
            ErrorCode.EXPIRED_TOKEN -> HttpStatus.UNAUTHORIZED
            ErrorCode.ACCESS_DENIED -> HttpStatus.FORBIDDEN
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.USER_NOT_FOUND -> HttpStatus.NOT_FOUND
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}

data class ErrorResponse(
    val code: String,
    val message: String
) {
    constructor(errorCode: ErrorCode) : this(
        code = errorCode.code,
        message = errorCode.message
    )
}

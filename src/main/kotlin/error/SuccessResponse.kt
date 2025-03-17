package error

data class SuccessResponse<T>(
    val success: Boolean = true,
    val data : T,
    val message: String ? = null
) {
    companion object {
        fun <T> of(data: T, message: String? = null): SuccessResponse<T> {
            return SuccessResponse(true, data, message)
        }

        fun empty(message: String? = null): SuccessResponse<Unit> {
            return SuccessResponse(data = Unit, message = message)
        }
    }
}

package hu.bsstudio.robonaut.security

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class RobonAuthFilter(private val apiKey: String) : HandlerFilterFunction<ServerResponse, ServerResponse> {

    override fun filter(request: ServerRequest, handlerFunction: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        if (apiKeyDoesNotMatchOrEmpty(request)) {
            LOG.error("Api key does not match or empty")
            return UNAUTHORIZED_RESPONSE
        }
        return handlerFunction.handle(request)
    }

    private fun apiKeyDoesNotMatchOrEmpty(request: ServerRequest): Boolean {
        return !getHeaderValues(request, AUTH_API_KEY_HEADER).contains(apiKey)
    }

    private fun getHeaderValues(request: ServerRequest, headerName: String): List<String> {
        return request.headers().header(headerName)
    }

    companion object {
        private val UNAUTHORIZED_RESPONSE = ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
        private val LOG = LoggerFactory.getLogger(RobonAuthFilter::class.java)
        private const val AUTH_API_KEY_HEADER = "RobonAuth-Api-Key"
    }
}

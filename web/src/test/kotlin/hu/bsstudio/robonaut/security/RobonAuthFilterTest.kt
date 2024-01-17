package hu.bsstudio.robonaut.security

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class RobonAuthFilterTest {
    @MockK
    private lateinit var request: ServerRequest

    @MockK
    private lateinit var handlerFunction: HandlerFunction<ServerResponse>

    @MockK
    private lateinit var mockResponse: ServerResponse

    @MockK
    private lateinit var headers: ServerRequest.Headers

    private lateinit var underTest: RobonAuthFilter

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = RobonAuthFilter(APPLICATION_SECRET)
    }

    @Test
    internal fun `should return Unauthorized when application secret is not present`() {
        every { request.headers() } returns headers
        every { headers.header(APPLICATION_SECRET_HEADER) } returns emptyList()

        val response = underTest.filter(request, handlerFunction)

        StepVerifier.create(response)
            .assertNext { assertThat(it.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED) }
            .verifyComplete()
    }

    @Test
    internal fun `should return Unauthorized when application secret is not matching`() {
        every { request.headers() } returns headers
        every { headers.header(APPLICATION_SECRET_HEADER) } returns listOf(OTHER_APPLICATION_SECRET)

        val response = underTest.filter(request, handlerFunction)

        StepVerifier.create(response)
            .assertNext { assertThat(it.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED) }
            .verifyComplete()
    }

    @Test
    internal fun `should return request when user is authenticated`() {
        every { request.headers() } returns headers
        every { headers.header(APPLICATION_SECRET_HEADER) } returns listOf(APPLICATION_SECRET)
        every { handlerFunction.handle(request) } returns Mono.just(mockResponse)

        val response = underTest.filter(request, handlerFunction)

        StepVerifier.create(response)
            .expectNext(mockResponse)
            .verifyComplete()
    }

    companion object {
        private const val APPLICATION_SECRET_HEADER = "RobonAuth-Api-Key"
        private const val APPLICATION_SECRET = "applicationSecret"
        private const val OTHER_APPLICATION_SECRET = "otherApplicationSecret"
    }
}

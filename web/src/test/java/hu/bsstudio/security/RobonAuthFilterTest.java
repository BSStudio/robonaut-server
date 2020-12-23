package hu.bsstudio.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class RobonAuthFilterTest {

    private static final String APPLICATION_SECRET_HEADER = "RobonAuth-Api-Key";
    private static final String APPLICATION_SECRET = "applicationSecret";
    private static final String OTHER_APPLICATION_SECRET = "otherApplicationSecret";

    @Mock
    private ServerRequest request;
    @Mock
    private HandlerFunction<ServerResponse> handlerFunction;
    @Mock
    private ServerResponse mockResponse;
    @Mock
    private ServerRequest.Headers headers;

    private RobonAuthFilter underTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.underTest = new RobonAuthFilter(APPLICATION_SECRET);
    }

    @Test
    void shouldReturnUnauthorizedWhenApplicationSecretIsNotPresent() {
        when(request.headers()).thenReturn(headers);
        when(headers.header(APPLICATION_SECRET_HEADER)).thenReturn(Collections.emptyList());

        final var response = underTest.filter(request, handlerFunction);

        StepVerifier.create(response)
            .assertNext(serverResponse -> assertThat(serverResponse.statusCode(), is(equalTo(HttpStatus.UNAUTHORIZED))))
            .verifyComplete();
    }

    @Test
    void shouldReturnUnauthorizedWhenApplicationSecretIsNotMatching() {
        when(request.headers()).thenReturn(headers);
        when(headers.header(APPLICATION_SECRET_HEADER)).thenReturn(List.of(OTHER_APPLICATION_SECRET));

        final var response = underTest.filter(request, handlerFunction);

        StepVerifier.create(response)
            .assertNext(serverResponse -> assertThat(serverResponse.statusCode(), is(equalTo(HttpStatus.UNAUTHORIZED))))
            .verifyComplete();
    }

    @Test
    void shouldReturnRequestWhenUserIsAuthenticated() {
        when(request.headers()).thenReturn(headers);
        when(headers.header(APPLICATION_SECRET_HEADER)).thenReturn(List.of(APPLICATION_SECRET));
        when(handlerFunction.handle(request)).thenReturn(Mono.just(mockResponse));

        final var response = underTest.filter(request, handlerFunction);

        StepVerifier.create(response)
            .expectNext(mockResponse)
            .verifyComplete();
    }
}

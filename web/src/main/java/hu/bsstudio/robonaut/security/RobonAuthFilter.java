package hu.bsstudio.robonaut.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RobonAuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final Mono<ServerResponse> UNAUTHORIZED_RESPONSE = ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
    private static final String AUTH_API_KEY_HEADER = "RobonAuth-Api-Key";

    private final String apiKey;

    @Override
    public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> handlerFunction) {
        if (apiKeyDoesNotMatchOrEmpty(request)) {
            LOG.error("Api key does not match or empty");
            return UNAUTHORIZED_RESPONSE;
        }
        return handlerFunction.handle(request);
    }

    private boolean apiKeyDoesNotMatchOrEmpty(final ServerRequest request) {
        return !getHeaderValues(request, AUTH_API_KEY_HEADER).contains(apiKey);
    }

    private List<String> getHeaderValues(final ServerRequest request, final String headerName) {
        return request.headers().header(headerName);
    }
}

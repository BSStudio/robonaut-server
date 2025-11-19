package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class SafetyCarFollowHandler(
  private val service: SafetyCarService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono<SafetyCarFollowInformation>()
      .flatMap(service::safetyCarWasFollowed)
      .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
}

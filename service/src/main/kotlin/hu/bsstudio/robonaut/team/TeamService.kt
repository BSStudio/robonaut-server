package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Team
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TeamService {
    fun addTeam(team: Team): Mono<DetailedTeam>

    fun updateTeam(team: Team): Mono<DetailedTeam>

    fun updateTeam(detailedTeam: DetailedTeam): Mono<DetailedTeam>

    fun findAllTeam(): Flux<DetailedTeam>
}

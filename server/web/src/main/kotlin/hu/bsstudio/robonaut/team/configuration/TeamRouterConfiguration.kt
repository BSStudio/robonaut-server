package hu.bsstudio.robonaut.team.configuration

import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.team.AdminUpdateTeamHandler
import hu.bsstudio.robonaut.team.CreateTeamHandler
import hu.bsstudio.robonaut.team.ReadAllTeamHandler
import hu.bsstudio.robonaut.team.TeamService
import hu.bsstudio.robonaut.team.UpdateTeamHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class TeamRouterConfiguration(
    private val teamService: TeamService,
    private val robonAuthFilter: RobonAuthFilter,
) {
    @Bean
    fun teamRouterFunction(
        createTeamHandler: CreateTeamHandler,
        readAllTeamHandler: ReadAllTeamHandler,
        adminUpdateTeamHandler: AdminUpdateTeamHandler,
        updateTeamHandler: UpdateTeamHandler,
    ): RouterFunction<ServerResponse> =
        RouterFunctions
            .route()
            .filter(robonAuthFilter)
            .POST("/api/team", createTeamHandler)
            .GET("/api/team", readAllTeamHandler)
            .PUT("/api/team", updateTeamHandler)
            .PUT("/api/admin/team", adminUpdateTeamHandler)
            .build()

    @Bean
    fun createTeamHandler() = CreateTeamHandler(teamService)

    @Bean
    fun readAllTeamHandler() = ReadAllTeamHandler(teamService)

    @Bean
    fun updateTeamHandler() = UpdateTeamHandler(teamService)

    @Bean
    fun adminUpdateTeamHandler() = AdminUpdateTeamHandler(teamService)
}

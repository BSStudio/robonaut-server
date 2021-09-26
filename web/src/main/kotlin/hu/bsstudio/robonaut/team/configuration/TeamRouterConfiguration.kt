package hu.bsstudio.robonaut.team.configuration

import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.team.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class TeamRouterConfiguration(
    @Autowired private val teamService: TeamService,
    @Autowired private val robonAuthFilter: RobonAuthFilter
) {

    @Bean
    fun teamRouterFunction(
        createTeamHandler: CreateTeamHandler,
        readAllTeamHandler: ReadAllTeamHandler,
        adminUpdateTeamHandler: AdminUpdateTeamHandler,
        updateTeamHandler: UpdateTeamHandler
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/team", createTeamHandler)
            .GET("/api/team", readAllTeamHandler)
            .PUT("/api/team", updateTeamHandler)
            .PUT("/api/admin/team", adminUpdateTeamHandler)
            .build()
    }

    @Bean
    fun createTeamHandler(): CreateTeamHandler = CreateTeamHandler(teamService)

    @Bean
    fun readAllTeamHandler(): ReadAllTeamHandler = ReadAllTeamHandler(teamService)

    @Bean
    fun updateTeamHandler(): UpdateTeamHandler = UpdateTeamHandler(teamService)

    @Bean
    fun adminUpdateTeamHandler(): AdminUpdateTeamHandler = AdminUpdateTeamHandler(teamService)
}

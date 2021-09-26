package hu.bsstudio.robonaut.team.model

import hu.bsstudio.robonaut.entity.TeamType
import lombok.Builder
import lombok.Value
import lombok.extern.jackson.Jacksonized

@Value
@Builder
@Jacksonized
class Team {
    var teamId: Long = 0
    var year = 0
    var teamName: String? = null
    var teamMembers: List<String>? = null
    var teamType: TeamType? = null
}

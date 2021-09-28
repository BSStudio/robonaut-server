package hu.bsstudio.robonaut.repository

import hu.bsstudio.robonaut.entity.TeamEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TeamRepository : ReactiveMongoRepository<TeamEntity, Long>

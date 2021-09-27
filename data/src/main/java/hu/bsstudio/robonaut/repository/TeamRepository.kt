package hu.bsstudio.robonaut.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import hu.bsstudio.robonaut.entity.TeamEntity

interface TeamRepository : ReactiveMongoRepository<TeamEntity, Long>

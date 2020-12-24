package hu.bsstudio.robonaut.repository;

import hu.bsstudio.robonaut.entity.TeamEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TeamRepository extends ReactiveMongoRepository<TeamEntity, Long> {
}

package hu.bsstudio.repository;

import hu.bsstudio.entity.TeamEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TeamRepository extends ReactiveMongoRepository<TeamEntity, Long> {
}

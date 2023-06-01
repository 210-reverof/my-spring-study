package wy.practice.neo4j.repository;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import reactor.core.publisher.Mono;
import wy.practice.neo4j.data.entity.Developer;
public interface DeveloperRepository extends ReactiveNeo4jRepository<Developer, String> {
    Mono<Developer> findOneByName(String name);
}

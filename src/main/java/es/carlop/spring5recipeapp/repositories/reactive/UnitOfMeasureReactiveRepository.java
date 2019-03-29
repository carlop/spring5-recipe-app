package es.carlop.spring5recipeapp.repositories.reactive;

import es.carlop.spring5recipeapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
}

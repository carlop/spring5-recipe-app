package es.carlop.spring5recipeapp.services;

import es.carlop.spring5recipeapp.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUoms();
}

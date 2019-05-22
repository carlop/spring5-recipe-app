package es.carlop.spring5recipeapp.services;

import es.carlop.spring5recipeapp.commands.RecipeCommand;
import es.carlop.spring5recipeapp.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String id);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

    Mono<RecipeCommand> findCommandById(String id);

    void deleteById(String id);
}

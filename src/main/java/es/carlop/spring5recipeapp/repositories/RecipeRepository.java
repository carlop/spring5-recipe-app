package es.carlop.spring5recipeapp.repositories;

import es.carlop.spring5recipeapp.domain.Recipe;

import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}

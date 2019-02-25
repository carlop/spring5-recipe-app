package es.carlop.spring5recipeapp.services;

import es.carlop.spring5recipeapp.commands.RecipeCommand;
import es.carlop.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findById(String id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand findCommandById(String l);

    void deleteById(String id);
}

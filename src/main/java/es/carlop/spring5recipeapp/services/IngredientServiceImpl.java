package es.carlop.spring5recipeapp.services;

import es.carlop.spring5recipeapp.commands.IngredientCommand;
import es.carlop.spring5recipeapp.converters.IngredientCommandToIngredient;
import es.carlop.spring5recipeapp.converters.IngredientToIngredientCommand;
import es.carlop.spring5recipeapp.domain.Ingredient;
import es.carlop.spring5recipeapp.domain.Recipe;
import es.carlop.spring5recipeapp.domain.UnitOfMeasure;
import es.carlop.spring5recipeapp.repositories.RecipeRepository;
import es.carlop.spring5recipeapp.repositories.reactive.RecipeReactiveRepository;
import es.carlop.spring5recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeReactiveRepository recipeReactiveRepository, RecipeRepository recipeRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
//    @Transactional
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if (!recipeOptional.isPresent()) {
            // Todo toss error if not found!
            log.error("Recipe not found for id: " + ingredientCommand.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUom(unitOfMeasureReactiveRepository
                        .findById(ingredientCommand.getUom().getId()).block());
//                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); // Todo address this

                if (ingredientFound.getUom() == null) {
                    new RuntimeException("UOM NOT FOUND");
                }

            } else {
                // add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                if (ingredient.getId().isEmpty()) {
                    ingredient.setId(UUID.randomUUID().toString());
                }
                if (ingredient.getUom().getDescription() == null) {
                    ingredient.getUom().setDescription(unitOfMeasureReactiveRepository.
                            findById(ingredient.getUom().getId()).block().getDescription());
                }
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            // Check by description
            if (!savedIngredientOptional.isPresent()) {
                // Not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(ingredientCommand.getUom().getId()))
                        .findFirst();
            }

            // To do check for fail

            // Enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());

            return Mono.just(ingredientCommandSaved);

        }
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientIdToDelete) {
        log.debug("Deleting ingredient: " + ingredientIdToDelete + " from recipe " + recipeId);

        Recipe recipe = recipeRepository.findById(recipeId).get();

        if (recipe != null) {
            log.debug("Found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientIdToDelete))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                log.debug("Found ingredient");
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe with id " + recipeId + " not found");
        }

        return Mono.empty();
    }
}

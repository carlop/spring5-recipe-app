package es.carlop.spring5recipeapp.repositories.reactive;

import es.carlop.spring5recipeapp.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryTest {

    private static final String YUMMY = "Yummy";

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Before
    public void setUp() throws Exception {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void testRecipeSave() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription(YUMMY);

        recipeReactiveRepository.save(recipe).block();

        Long count = recipeReactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription(YUMMY);

        recipeReactiveRepository.save(recipe).block();

        Recipe fetchedRecipe = recipeReactiveRepository.findByDescription(YUMMY).block();

        assertEquals(YUMMY, fetchedRecipe.getDescription());
    }
}
package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.businessLayer.ID;
import recipes.businessLayer.Recipe;
import recipes.businessLayer.RecipeService;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private ID id;

    @PostMapping("/recipe/new")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe recipeCreate = recipeService.save(new Recipe(recipe.getName(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections()));
        this.id.setId(recipeCreate.getId());
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }


    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable @Pattern(regexp = "[0-9]+") Long id) {
        if (recipeService.findRecipeById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.findRecipeById(id));
        } else {
            throw new RecipeNotFoundException();
        }
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+")Long id) {
        if (recipeService.findRecipeById(id).isPresent()) {
            recipeService.deleteById(id);
            throw  new NoContentRecipeException();
        } else {
            throw new RecipeNotFoundException();
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    class RecipeNotFoundException extends RuntimeException {
    }
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    class NoContentRecipeException extends RuntimeException {
    }
}

package recipes.presentation;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.businessLayer.ID;
import recipes.businessLayer.Recipe;
import recipes.businessLayer.RecipeService;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private ID id;

    @PostMapping("/recipe/new")

    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe recipeCreate = new Recipe(recipe.getName(),
                                         recipe.getCategory(),
                                         recipe.getDescription(),
                                         recipe.getIngredients(),
                                         recipe.getDirections());
        recipeCreate.setDate(LocalDateTime.now());
        recipeService.save(recipeCreate);
        this.id.setId(recipeCreate.getId());
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }


    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id) {
        if (recipeService.findRecipeById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.findRecipeById(id));
        } else {
            throw new RecipeNotFoundException();
        }
    }

    /**
     * @param category
     * @return
     */
    @GetMapping("/recipe/search/")
    public ResponseEntity<?> getRecipesByCategoryOrName(@Valid @RequestParam(required = false) @Nullable  String category,
                                                        @Valid @RequestParam(required = false) @Nullable  String name) {
//        if (recipeService.findByCategory(category)) {
        if (category == null && name == null) {
            throw new SearchParametersException();
        }
        if (name == null) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.findByCategory(category));
        } else if (category == null) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.findByName(name));
        } else {
            throw new SearchParametersException();
        }
    }



    @PutMapping("/recipe/{id}")
    public ResponseEntity<?> putRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id,
                                       @Valid @RequestBody Recipe recipe) {
        if (recipeService.findRecipeById(id).isPresent()) {
            recipe.setDate(LocalDateTime.now());
            recipe.setId(id);
            recipeService.save(recipe);
            throw  new NoContentRecipeException();
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
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    class SearchParametersException extends RuntimeException {

    }


}

package recipes.presentation;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.recipe.ID;
import recipes.business.recipe.Recipe;
import recipes.business.recipe.RecipeService;
import recipes.business.user.User;
import recipes.business.user.UserService;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;
    @Autowired
    private ID id;

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe,
                                       @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findUserByEmail(currentUser.getUsername());
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(user);

        Long recipeId = recipeService.save(recipe).getId();
        this.id.setId(recipeId);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> putRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id,
                                       @Valid @RequestBody Recipe recipe,
                                       @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findUserByEmail(currentUser.getUsername());

        authorization(id,user);
        recipe.setDate(LocalDateTime.now());
        recipe.setId(id);
        recipeService.save(recipe);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Content");
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id,
                                          @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findUserByEmail(currentUser.getUsername());
        authorization(id,user);

        recipeService.deleteById(id);

        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Content");
    }
    public void authorization(Long id, User user) {
        if (!recipeService.existsRecipeById(id)) {
            throw new RecipeNotFoundException();
        } else if (recipeService.findRecipeById(id).getUser() != null) {
            if (!recipeService.findRecipeById(id).getUser().getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.findRecipeById(id));

    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> getRecipesByCategoryOrName(@Valid @RequestParam(required = false) @Nullable String category,
                                                        @Valid @RequestParam(required = false) @Nullable String name) {
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

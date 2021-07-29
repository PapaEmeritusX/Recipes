package recipes.presentation;

import io.micrometer.core.lang.Nullable;
import org.apache.coyote.Response;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.auth.AuthenticationFacade;
import recipes.business.auth.IAuthenticationFacade;
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
//    private AuthenticationFacade authenticationFacade;
    @Autowired
    private ID id;

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe,
                                       @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByEmail(currentUser.getUsername());
        Recipe recipeCreate = new Recipe(recipe.getName(),
                                         recipe.getCategory(),
                                         recipe.getDescription(),
                                         recipe.getIngredients(),
                                         recipe.getDirections());
        recipeCreate.setDate(LocalDateTime.now());
        recipeCreate.setUser(user);

        Recipe toAddRecipe = recipeService.save(recipeCreate);
        user.getRecipes().add(toAddRecipe);
        System.out.println(user);
        this.id.setId(recipeCreate.getId());

        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id,
                                          @Valid @AuthenticationPrincipal UserDetails currentUser) {


        User user = userService.findByEmail(currentUser.getUsername());
        System.out.println(recipeService.findRecipeById(id));
        if (!recipeService.existsRecipeById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        else if (!recipeService.findRecipeById(id).getUser().getEmail().equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");

        recipeService.deleteById(id);

        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Content");
    }


    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id) {
        if (recipeService.findRecipeById(id) != null) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.findRecipeById(id));
        } else {
            throw new RecipeNotFoundException();
        }
    }

    /**
     * @param category
     * @return
     */
    @GetMapping("/api/recipe/search")
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



    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> putRecipe(@PathVariable("id") @Pattern(regexp = "[0-9]+") Long id,
                                       @Valid @RequestBody Recipe recipe,
                                       @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByEmail(currentUser.getUsername());

        if (!recipeService.existsRecipeById(id)) {
            throw new RecipeNotFoundException();
        } else if (!recipeService.findRecipeById(id).getUser().getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

            recipe.setDate(LocalDateTime.now());
            recipe.setId(id);
            recipeService.save(recipe);
            user.getRecipes().add(recipe);
            throw  new NoContentRecipeException();


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

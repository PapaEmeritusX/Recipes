package recipes.business.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.auth.IAuthenticationFacade;
import recipes.persistence.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {


    private RecipeRepository recipeRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Recipe save(Recipe recipe) {

        return recipeRepository.save(recipe);
    }

    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    };

    public List<Recipe> findByCategory(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }
    public List<Recipe> findByName(String name) {
        return recipeRepository.findAllByNameIgnoreCaseContainingOrderByDateDesc(name);
    }

    public boolean existsRecipeById(Long id) {
        return recipeRepository.existsById(id);
    }
}

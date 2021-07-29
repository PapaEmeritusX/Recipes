package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.recipe.Recipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long>  {

    Optional<Recipe> findById(Long id);

    void deleteById(Long id);

    List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findAllByNameIgnoreCaseContainingOrderByDateDesc(String category);

    @Override
    boolean existsById(Long aLong);
}

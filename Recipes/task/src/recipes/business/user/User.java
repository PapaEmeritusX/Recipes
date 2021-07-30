package recipes.business.user;

import lombok.*;
import recipes.business.recipe.Recipe;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Email
    @NotBlank
    @Pattern(regexp = ".*@.*\\..+")
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>();

    @Column(name = "role")
    private String roles = "USER";

    public String getEmail() {
        return this.email;
    }

    public void addRecipe(Recipe recipe)
    {
        recipes.add(recipe);
    }
}
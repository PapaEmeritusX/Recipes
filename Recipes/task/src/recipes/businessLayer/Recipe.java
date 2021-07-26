package recipes.businessLayer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Recipe")
public class Recipe {

    @Id
    @JsonIgnore()
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private String name;


    @NotNull
    @NotBlank
    private String category;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "dd.MM.YYYY HH:mm:ss.SSSSSS")
    private LocalDateTime date;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @Size(min = 1)
    @ElementCollection
    private List<String> ingredients;

    @NotNull
    @Size(min = 1)
    @ElementCollection
    private List<String> directions;


    public Recipe(String name,
                  String category,
                  String description,
                  List<String> ingredients,
                  List<String> directions) {

        this.name = name;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }

}

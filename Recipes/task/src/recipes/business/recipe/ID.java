package recipes.business.recipe;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class ID {


    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

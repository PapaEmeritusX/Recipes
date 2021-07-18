import com.google.gson.Gson;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;

import recipes.RecipesApplication;

import java.util.Map;

import static org.hyperskill.hstest.testcase.CheckResult.correct;


public class RecipesApplicationTest extends SpringTest {

    public RecipesApplicationTest() {
        super(RecipesApplication.class);
    }

    final Map.Entry<String, String> name = Map.entry("name", "Fresh Mint Tea /Test");
    final Map.Entry<String, String> description = Map.entry("description", "Light, aromatic and refreshing beverage, ... /Test");
    final Map.Entry<String, String> ingredients = Map.entry("ingredients", "boiled water, honey, fresh mint leaves /Test");
    final Map.Entry<String, String> directions = Map.entry("directions", "1) Boil water. 2) Pour boiling hot water into a mug. " +
        "3) Add fresh mint leaves. 4) Mix and let the mint leaves seep for 3-5 minutes. 5) Add honey and mix again. /Test");

    final String JSON_TEST_RECIPE = new Gson().toJson(
        Map.ofEntries(name, description, ingredients, directions)
    );

    final String API_RECIPE = "/api/recipe";


    static void throwIfIncorrectStatusCode(HttpResponse response, int status) {
        if (response.getStatusCode() != status) {
            throw new WrongAnswer(response.getRequest().getMethod() +
                " " + response.getRequest().getLocalUri() +
                " should respond with status code " + status +
                ", responded: " + response.getStatusCode() + "\n\n" +
                "Response body:\n" + response.getContent());
        }
    }


    @DynamicTest
    DynamicTesting[] dt = new DynamicTesting[]{
        this::testPostRecipe,
        this::testGetRecipe
    };

    CheckResult testPostRecipe() {
        HttpResponse response = post(API_RECIPE, JSON_TEST_RECIPE).send();

        throwIfIncorrectStatusCode(response, 200);

        return correct();
    }

    CheckResult testGetRecipe() {
        HttpResponse response = get(API_RECIPE).send();

        throwIfIncorrectStatusCode(response, 200);

        expect(response.getContent()).asJson().check(
            isObject()
                .value(name.getKey(), isString(name.getValue()))
                .value(description.getKey(), isString(description.getValue()))
                .value(ingredients.getKey(), isString(ingredients.getValue()))
                .value(directions.getKey(), isString(directions.getValue())));

        return correct();
    }
}

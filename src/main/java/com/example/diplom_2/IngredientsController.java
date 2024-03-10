package com.example.diplom_2;

import com.example.diplom_2.fixtures.Ingredient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

import java.util.stream.Collectors;

import static com.example.diplom_2.Config.*;
import static io.restassured.RestAssured.given;
import java.util.List;

public class IngredientsController {

    @Step("Получение хешей ингридиентов")
    public static String[] getIngredientsIds (int count) {
        List<String> response = given()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body().jsonPath()
                .getList("data", Ingredient.class)
                .stream().map(Ingredient::get_id)
                .collect(Collectors.toList());

        String[] result = new String[count];
        for(int i = 0; i < count; i++) {
            result[i] = response.get(i);
        }

        return result;
    }
}

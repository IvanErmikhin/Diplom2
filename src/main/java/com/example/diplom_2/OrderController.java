package com.example.diplom_2;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.example.diplom_2.Config.*;
import static io.restassured.RestAssured.given;

public class OrderController {

    @Step("Создание нового заказа")
    public static Response newOrder(CreateOrder createOrder) {
        return given()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .body(createOrder)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение данных по заказу")
    public static Response getOrder(String token, boolean useAuth) {
        Response response;
        if (useAuth) {
            response = given()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .auth().oauth2(token)
                    .when()
                    .get(ORDERS_PATH);
        } else {
            response = given()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .when()
                    .get(ORDERS_PATH);
        }
        return response;
    }
}
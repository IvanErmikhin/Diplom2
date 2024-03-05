import com.example.diplom_2.CreateOrder;
import com.example.diplom_2.LoginUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static com.example.diplom_2.Config.*;
import static com.example.diplom_2.OrderController.newOrder;
import static com.example.diplom_2.UserController.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {

    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Проверка создания заказа")
    @Description("Действительный идентификатор пользователя и ингредиенты должны возвращать 200 и тело")
    public void createValidOrderTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(new LoginUser(CREATE_USER.getEmail(), CREATE_USER.getPassword()));
        Response response = newOrder(CREATE_ORDER);
        response.then().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Заказ со всеми ингредиентами и без авторизации пользователя должны возвращать 200 и тело")
    public void successCreateOrderWithAuthAndWithIngredientsTest() {
        Response response = newOrder(CREATE_ORDER);
        response.then().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    @Description("Заказ со всеми ингредиентами и без авторизации пользователя должны возвращать 200 и тело")
    public void successCreateOrderWithoutAuthAndWithIngredientsTest() {
        Response response = newOrder(CREATE_ORDER);
        response.then().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    @Description("Недопустимые ингредиенты и действительная авторизация пользователя должны возвращать 400 и сообщение")
    public void successCreateOrderWithAuthAndWithoutIngredientsTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        CreateOrder createOrder = new CreateOrder(new String[]{});
        Response response = newOrder(createOrder);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("Без ингредиентов и без авторизации пользователя должно быть возвращено 400 и сообщение")
    public void successCreateOrderWithoutAuthAndWithoutIngredientsTest() {
        Response response = newOrder(EMPTY_ORDER);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хэшем ингредиентов")
    @Description("Недопустимые ингредиенты и с авторизацией пользователя должны возвращать 500")
    public void successCreateOrderWithAuthAndWithWrongIngredientsTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        Response response = newOrder(INVALID_INGREDIENT_ORDER);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и неверным хэшем ингредиентов")
    @Description("Недопустимые ингредиенты и без авторизации пользователя должны возвращать 500")
    public void successCreateOrderWithoutAuthAndWithWrongIngredientsTest() {
        Response response = newOrder(INVALID_INGREDIENT_ORDER);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @AfterClass
    public static void cleanUp() {
        deleteUser(token);
    }
}

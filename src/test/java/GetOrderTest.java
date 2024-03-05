import com.example.diplom_2.OrderController;
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
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Действительный логин пользователя должен возвращать 200 и заказы")
    public void getUserOrdersTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        newOrder(CREATE_ORDER);
        Response response = OrderController.getOrder(token, true);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("orders", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Неверный логин пользователя должен возвращать код состояния 401 и сообщение")
    public void getInvalidUserTest() {
        Response response = OrderController.getOrder(token,false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @AfterClass
    public static void cleanUp() {
        deleteUser(token);
    }
}

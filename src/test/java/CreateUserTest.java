import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.example.diplom_2.Config.*;
import static com.example.diplom_2.UserController.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    @Description("Регистрация нового пользователя должна возвращать 200 и тело")
    public void newUserRegistrationTest() {
        Response response = createNewUser(CREATE_USER);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(CREATE_USER.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and().body("accessToken", startsWith("Bearer"))
                .and().body("refreshToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Попытка регистрации зарегистрированного пользователя должна возвращать 403 и сообщение")
    public void sameUserRegistrationTest() {
        createNewUser(CREATE_USER);
        Response response = createNewUser(CREATE_USER);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @After
    public void cleanUp() {
        deleteUser(LOGIN_USER);
    }

}

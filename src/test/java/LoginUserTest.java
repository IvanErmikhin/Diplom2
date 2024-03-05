import com.example.diplom_2.LoginUser;
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

public class LoginUserTest {
    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Успешный ответ текущего пользователя на вход в систему с основными значениями")
    public void validUserLoginTest() {
        Response response = loginUser(LOGIN_USER);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("accessToken", startsWith("Bearer"))
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(LOGIN_USER.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Не верный логин пользователя должно возвращать код состояния 401")
    public void invalidLoginTest() {
        LoginUser invalidLogin = new LoginUser(faker.internet().emailAddress(), PASSWORD);
        checkInvalidCredential(invalidLogin);
    }
    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Не верный пароль пользователя должно возвращать код состояния 401")
    public void invalidPasswordTest() {
        LoginUser invalidPassword = new LoginUser(EMAIL,faker.internet().password());
        checkInvalidCredential(invalidPassword);
    }

    @After
    public void cleanUp() {
        deleteUser(token);
    }
}

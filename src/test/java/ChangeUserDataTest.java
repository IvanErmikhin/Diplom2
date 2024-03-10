import com.example.diplom_2.ChangeUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static com.example.diplom_2.Config.*;

import static com.example.diplom_2.Config.APP_URL;
import static com.example.diplom_2.UserController.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataTest {
    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
    }

    @Test
    @DisplayName("Изменение e-mail пользователя с авторизацией")
    public void successChangeUserEmailWithAuthTest() {
        loginUser(LOGIN_USER);
        ChangeUser changeUserEmail = new ChangeUser(EMAIL, CREATE_USER.getName());
        Response response = changeUserData(LOGIN_USER,changeUserEmail, true);
        response.then().assertThat()
                .and().body("user.email", equalTo(changeUserEmail.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void successChangeUserNameWithAuthTest() {
        loginUser(LOGIN_USER);
        Response response = changeUserData(LOGIN_USER,CHANGE_USER_NAME, true);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(CREATE_USER.getEmail()))
                .and().body("user.name", equalTo(CHANGE_USER_NAME.getName()))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение e-mail пользователя без авторизации")
    public void changeUserEmailWithoutAuthTest() {
        loginUser(LOGIN_USER);
        Response response = changeUserData(LOGIN_USER,CHANGE_USER_EMAIL, false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }
    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthTest() {
        loginUser(LOGIN_USER);
        Response response = changeUserData(LOGIN_USER,CHANGE_USER_NAME, false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void cleanUp() {
        deleteUser(token);
    }
}

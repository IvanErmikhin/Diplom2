package com.example.diplom_2;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static com.example.diplom_2.Config.*;

public class UserController {
    public static Response createNewUser(CreateUser createUser) {
        return given()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .body(createUser)
                .when()
                .post(CREATE_USER_PATH);
    }

    public static Response deleteUser(LoginUser loginUser) {
        return given()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .auth().oauth2(getUserToken(loginUser))
                .when()
                .delete(DELETE_USER_PATH);
    }

    public static Response deleteUser(String token) {
        return given()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .auth().oauth2(token)
                .when()
                .delete(DELETE_USER_PATH);
    }

    public static String getUserToken(LoginUser loginUser) {
        Response response = loginUser(loginUser);
        String accessToken = response.jsonPath().get("accessToken");
        return accessToken.replace("Bearer ","");

    }
    public static Response loginUser(LoginUser loginUser) {
        return
                given()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(loginUser)
                        .when()
                        .post(LOGIN_USER_PATH);
    }

    public static Response changeUserData(LoginUser userBefore, ChangeUser changeUser, boolean useAuth) {
        String token;
        Response response;
        if (useAuth) {
            token = getUserToken(userBefore);
            response = given()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .auth().oauth2(token)
                    .body(changeUser)
                    .when()
                    .patch(CHANGE_USER_PATH);
        } else {
            response = given()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .body(changeUser)
                    .when()
                    .patch(CHANGE_USER_PATH);
        }
        return response;
    }
    public static  void checkInvalidCredential(LoginUser credential) {
        Response response = loginUser(credential);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
}
package com.example.diplom_2;
import com.github.javafaker.Faker;

import static com.example.diplom_2.IngredientsController.getIngredientsIds;

public class Config {
    // main link
    public final static String APP_URL = "https://stellarburgers.nomoreparties.site";

    // path ...
    public final static String CREATE_USER_PATH = "/api/auth/register";
    public final static String LOGIN_USER_PATH = "/api/auth/login ";
    public final static String DELETE_USER_PATH = "/api/auth/user";
    public final static String CHANGE_USER_PATH = "/api/auth/user";
    public final static String ORDERS_PATH = "/api/orders";

    public final static String INGREDIENTS_PATH = "/api/ingredients";
    public final static String HEADER_CONTENT_TYPE_NAME = "Content-type";
    public final static String CONTENT_TYPE = "application/json";

    // fake data
    public static Faker faker = new Faker();
    public final static String EMAIL = "chucha@chu.chu";
    public final static String PASSWORD = "ThisIsPassword";
    public final static String NAME = "Misha";
    public final static CreateUser CREATE_USER = new CreateUser (EMAIL,PASSWORD,NAME);
    public final static LoginUser LOGIN_USER = new LoginUser(EMAIL,PASSWORD);
    public final static CreateOrder CREATE_ORDER = new CreateOrder(getIngredientsIds(2));
    public final static CreateOrder EMPTY_ORDER = new CreateOrder(new String[]{});
    public final static CreateOrder INVALID_INGREDIENT_ORDER = new CreateOrder(new String[]{"invalid63f7034a000269f45e7",});
    public final static  ChangeUser CHANGE_USER_NAME = new ChangeUser(CREATE_USER.getEmail(), faker.name().fullName());
    public final static ChangeUser CHANGE_USER_EMAIL = new ChangeUser(EMAIL, CREATE_USER.getName());

}

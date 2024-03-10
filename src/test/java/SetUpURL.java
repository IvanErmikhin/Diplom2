import io.restassured.RestAssured;
import org.junit.Before;

import static com.example.diplom_2.Config.APP_URL;

public class SetUpURL {
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

}

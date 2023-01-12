import io.restassured.RestAssured;
import org.junit.*;
import pojo.AuthCourier;
import pojo.Courier;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static Courier courier;
    @Test
    public void authCourierTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера
        AuthCourier authCourier = new AuthCourier(randomString, randomString);
        Utils.doPost(Constants.COURIER_INFO, authCourier)
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    public void authCourierWithWrongLoginTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера с неправильным login
        Utils.doPost(Constants.COURIER_INFO, "{\"login\":\"Anna\"," + "\"password\":\"" + courier.getPassword() + "\"}")
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void authCourierWithWrongPasswordTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера с неправильным Password
        Utils.doPost(Constants.COURIER_INFO, "{\"login\":\"" + courier.getLogin() + "\",\"password\":\"anna\"}")
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void authCourierWithoutLoginTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера без поля login
        Utils.doPost(Constants.COURIER_INFO, "{\"login\":\"\",\"password\":\"" + courier.getPassword() + "\"}")
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void authCourierWithEmptyLoginTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера с пустым полем login
        Utils.doPost(Constants.COURIER_INFO, "{\"password\":\"" + courier.getPassword() + "\"}")
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void authCourierWithoutPasswordTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера без поля password
        Utils.doPost(Constants.COURIER_INFO, "{\"login\":\"" + courier.getLogin() + "\"}")
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void authCourierWithEmptyPasswordTest() {
        //создание курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //авторизация курьера с пустым полем password
        Utils.doPost(Constants.COURIER_INFO, "{\"login\":\"" + courier.getLogin() + "\",\"password\":\"\"}")
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void authNonExistentCourierTest() {
        //авторизация несуществующим курьером
        String randomString = Utils.getRandomString(8);
        AuthCourier authCourier = new AuthCourier(randomString, randomString);
        Utils.doPost(Constants.COURIER_INFO, authCourier)
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo ("Учетная запись не найдена"));
    }

    @After
    public void deleteCouriers() {
        Utils.deleteCourier(courier);
    }
}
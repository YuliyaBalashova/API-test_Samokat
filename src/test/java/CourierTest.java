import io.restassured.RestAssured;
import org.junit.*;
import pojo.AuthCourier;
import pojo.Courier;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static Courier courier;
    @Test
    public void creatingCourierTest() {
        //создаем нового курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);

        //отправляем запрос на создание курьера
        Utils.createCourier(courier);
    }

    @Test
    public void creatingIdenticalCourierTest() {
        //создаем нового курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);

        //отправляем запрос на создание курьера
        Utils.createCourier(courier);

        //отправляем повторный запрос на создание курьера
        Utils.doPost(Constants.CREATE_COURIER, courier)
                .statusCode(SC_CONFLICT)
                .and()
                .body(equalTo("{\"message\": \"Этот логин уже используется\"}"));        // --> баг документации
               // .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void creatingCourierWithoutFirstNameTest() {
        //создаем нового курьера
        String newLogin = Utils.getRandomString(8);
        //создание курьера без поля firstName
        Utils.doPost(Constants.CREATE_COURIER, "{\"login\":\"" + newLogin + "\",\"password\":\"12345678asd\"}")
                .statusCode(SC_CREATED)
                .and()
                .body(equalTo("{\"ok\":true}"));
        courier = null;
    }

    @Test
    public void creatingCourierWithEmptyFirstNameTest() {
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, "");
        //создание курьера с пустым полем firstName
        Utils.createCourier(courier);
    }

    @Test
    public void creatingCourierWithoutPasswordTest() {
        String newLogin = Utils.getRandomString(7);
        //создание курьера без поля password
        Utils.doPost(Constants.CREATE_COURIER, "{\"login\":\"" + newLogin + "\",\"firstName\":\"Иванов Иван Иванович\"}")
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
             // .body(equalTo("{\"message\": \"Недостаточно данных для создания учетной записи\"}"));         --> был бы баг документации
    }

    @Test
    public void creatingCourierWithEmptyPasswordTest() {
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, "", randomString);
        //создание курьера с пустым полем password
        Utils.doPost(Constants.CREATE_COURIER, courier)
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
             // .body(equalTo("{\"message\": \"Недостаточно данных для создания учетной записи\"}"));         --> был бы баг документации
        courier=null;   // для метода delete
    }

    @Test
    public void creatingCourierWithoutLoginTest() {
        //создание курьера без поля login
        Utils.doPost(Constants.CREATE_COURIER, "{\"password\":\"12345678asd\",\"firstName\":\"Иванов Иван Иванович\"}")
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
             // .body(equalTo("{\"message\": \"Недостаточно данных для создания учетной записи\"}"));         --> был бы баг документации
        courier=null;   // для метода delete
    }

    @Test
    public void creatingCourierWithEmptyLoginTest() {
        String randomString = Utils.getRandomString(8);
        courier = new Courier("", randomString, randomString);
        //создание курьера с пустым полем password
        Utils.doPost(Constants.CREATE_COURIER, courier)
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
             // .body(equalTo("{\"message\": \"Недостаточно данных для создания учетной записи\"}"));         --> был бы баг документации
        courier=null; // для метода delete
    }

    @Test
    public void creatingCourierWithExistingLoginTest() {
        //создаем нового курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        //отправляем запрос на создание курьера
        Utils.createCourier(courier);

        //с имеющимся логином
        Utils.doPost(Constants.CREATE_COURIER, new Courier(courier.getLogin(), "8768kjhgfdsa", "Петров Петр Петрович"))
                .statusCode(SC_CONFLICT)
                .and()
                .body(equalTo("{\"message\": \"Этот логин уже используется\"}"));        // --> баг документации
             // .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void deleteCouriers() {
        Utils.deleteCourier(courier);
    }
}

import io.restassured.RestAssured;
import org.junit.*;
import pojo.AuthCourier;
import pojo.Courier;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static Courier courier;

    @Test
    public void getListOrdersTest() {

        // получить список всех заказов
        Utils.doGet(Constants.LIST_ORDERS)
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }

    @Test
    public void getListOrdersWithIdTest() {

        //  создаем курьера
        String randomString = Utils.getRandomString(8);
        courier = new Courier(randomString, randomString, randomString);
        Utils.createCourier(courier);

        //  авторизуем курьера и сохраняем его  id
        AuthCourier authCourier = new AuthCourier(courier.getLogin(), courier.getPassword());
        int id = Utils.doPost(Constants.COURIER_INFO, authCourier)
                .extract().body().jsonPath().get("id");

        //  выводим список заказов данного курьера
        given()
                .log().all()
                .header("Content-type", "application/json")
                .queryParam("courierId", id)
                .get(Constants.LIST_ORDERS)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());

        // удаляем курьера
        Utils.deleteCourier(id);
    }
}

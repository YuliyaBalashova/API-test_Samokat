import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.AuthCourier;
import pojo.Courier;

import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class Utils {

    private Utils() {
        throw new IllegalAccessError("Utility class");
    }

    public static ValidatableResponse doPost(String endpoint, Object body) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all();
    }

    public static ValidatableResponse doPost(String endpoint, String body) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all();
    }

    public static ValidatableResponse doGet(String endpoint) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .get(endpoint)
                .then()
                .log().all();
    }

    @Step("Создаем курьера")
    public static void createCourier(Courier courier) {
        //отправляем запрос на создание курьера
        doPost(Constants.CREATE_COURIER, courier)
                .statusCode(SC_CREATED)
                .and()
                .body(equalTo("{\"ok\":true}"));
    }

    public static void deleteCourier(Courier courier){
        if (courier != null) {
            AuthCourier authCourier = new AuthCourier(courier.getLogin(), courier.getPassword());
            int id = Utils.doPost(Constants.COURIER_INFO, authCourier)
                    .extract().body().jsonPath().get("id");
            given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .body("{\"id\":\"" + id +"\"}")
                    .when()
                    .delete(Constants.DELETE_COURIER + id)
                    .then()
                    .log().all();
        }
    }

    public static void deleteCourier(int id){
            given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .body("{\"id\":\"" + id +"\"}")
                    .when()
                    .delete(Constants.DELETE_COURIER + id)
                    .then()
                    .log().all();
    }

    /**
     * Получение рандомной строки
     * @param size - длина строки
     * @return - рандомная строка
     */
    public static String getRandomString(long size) {
        String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNOPRSTUYWYZ";
        return new Random().ints(size, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}

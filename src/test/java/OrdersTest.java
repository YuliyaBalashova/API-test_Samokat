import io.restassured.RestAssured;
import org.junit.*;
import pojo.Orders;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrdersTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public OrdersTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                { "Anna", "Ivanova", "Lenina 6", 1, "+7 999 888 77 66", 2, "2023-06-06", "Thanks", new String[]{"BLACK"}},
                { "Ivan", "Petrov", "Mira 6", 1, "+7 999 888 55 44", 1, "2023-05-06", "Thanks",  new String[]{"GRAY"}},
                { "Irina", "Petrova", "Mira 6", 1, "+7 999 888 33 22", 1, "2023-05-06", "Thanks",  new String[]{""}},
                { "Irina", "Petrova", "Mira 6", 1, "+7 999 888 33 22", 1, "2023-05-06", "Thanks",  new String[]{}},
                { "Ivan", "Ivanov", "Berezovaya 6", 1, "+7 999 888 11 11", 1, "2023-04-06", "Thanks", new String[]{"BLACK", "GRAY"}},
        };
    }

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private Orders order;
    @Test
    public void creatingOrdersTest() {
        order = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Utils.doPost(Constants.CREATE_ORDERS, order)
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue());
    }
}
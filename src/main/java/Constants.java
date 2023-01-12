public class Constants {

    private Constants() {
        throw new IllegalAccessError("Utility class");
    }

    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    public static final String CREATE_COURIER = "/api/v1/courier";
    public static final String COURIER_INFO = "/api/v1/courier/login";
    public static final String CREATE_ORDERS = "/api/v1/orders";
    public static final String DELETE_COURIER = "/api/v1/courier/";
    public static final String LIST_ORDERS = "/api/v1/orders";

}

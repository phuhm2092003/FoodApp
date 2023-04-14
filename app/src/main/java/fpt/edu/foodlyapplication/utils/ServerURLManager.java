package fpt.edu.foodlyapplication.utils;

public class ServerURLManager {
    public static final String URL_BASE = "http://192.168.0.108:3000";

    // User URLs
    public static final String URL_LOGIN = URL_BASE + "/login";
    public static final String URL_REGISTER_ACCOUNT = URL_BASE + "/registerAccounts";
    public static final String URL_GET_USER_BY_EMAIL = URL_BASE + "/getUserByEmail";
    public static final String URL_UPDATE_FULLNAME = URL_BASE + "/updateFullName";
    public static final String URL_UPDATE_PASSWORD = URL_BASE + "/updatePassword";

    // Product URLs
    public static final String URL_GET_LIST_PRODUCT = URL_BASE + "/listProduct";

    // Cart URLs
    public static final String URL_GET_LIST_CART = URL_BASE + "/listCart";
    public static final String URL_ADD_CART = URL_BASE + "/addCart";
    public static final String URL_DELETE_CART_BY_ID = URL_BASE + "/deleteCart";
    public static final String URL_DELETE_CART_BY_EMAIL = URL_BASE + "/deleteCartByEmail";
    public static final String URL_UPDATE_QUANTITY_CART = URL_BASE + "/updateQuantityCart";

    // Bill URL
    public static final String URL_ADD_BILL = URL_BASE + "/insertBill";
}

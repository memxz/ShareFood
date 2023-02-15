package iss.ad.team6.sharefood.utils;

public class Api {
    private static final String baseUrl = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service";
    public static final String api_register = baseUrl + "/api/individual/save";
    public static final String api_login = baseUrl + "/api/individual/authenticate";
    public static final String api_edit_account = baseUrl + "/api/individual/update";
    public static final String api_history = baseUrl + "/api/food/get-list/collected";
    public static final String api_all = baseUrl + "/api/food/get-all";
    public static final String api_edit_food = baseUrl + "/api/food/update";
}
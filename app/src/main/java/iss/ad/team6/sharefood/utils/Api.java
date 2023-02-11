package iss.ad.team6.sharefood.utils;

public class Api {
    private static final String baseUrl = "";
    public static final String api_register = baseUrl+ "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/individual/register";
    public static final String api_login =baseUrl+ "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/individual/authenticate";
    public static final String api_edit_account = baseUrl+ "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/individual/save";
}
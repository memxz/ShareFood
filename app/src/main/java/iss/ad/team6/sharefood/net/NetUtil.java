package iss.ad.team6.sharefood.net;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtil {
    public static final  String host = "https://v99xcpwju4.execute-api.ap-northeast-1.amazonaws.com/FoodDelieveryTest";
    public static  String userName ;
    public static String getData(JSONObject jsonObject, String url) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody
                = RequestBody.create(MediaType.parse("application/json"),
                jsonObject.toString());
        Request request = new Request.Builder()
                .url(host+url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String data = response.body().string();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

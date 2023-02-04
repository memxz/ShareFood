package iss.team6.sharefood.foodshare.utils;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class HttpUtil {

    public static OkHttpClient client = new OkHttpClient();
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void get(String url, Map<String, String> params, OnGetDataCallback callback) {
        String parseUrl = Utils.parseGetUrl(url,params);
        Request request = new Request.Builder().url(parseUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback!=null){
                            callback.onGetFailed(e.getMessage());
                        }
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback!=null){
                            callback.onGetSuccess(json);
                        }
                    }
                });

            }
        });
    }

    public static void post(String url, Map<String, String> params, OnGetDataCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params!=null){
            for (String key:params.keySet()){
                builder.add(key,params.get(key));
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback!=null){
                            callback.onGetFailed(e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback!=null){
                            callback.onGetSuccess(json);
                        }
                    }
                });

            }
        });
    }
    public static void post_json(String url,Map<String,String> params,OnGetDataCallback callback){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            if (params!=null){
                for (String key:params.keySet()){
                    json.put(key,params.get(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback!=null){
                            callback.onGetFailed(e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback!=null){
                            callback.onGetSuccess(json);
                        }
                    }
                });

            }
        });
    }

    public interface OnGetDataCallback {
        void onGetSuccess(String json);

        void onGetFailed(String msg);
    }
}


package iss.ad.team6.sharefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import java.io.IOException;

import iss.ad.team6.sharefood.Gson.Gson_User;
import iss.ad.team6.sharefood.Gson.Persons;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SplashActivity extends AppCompatActivity {

    private String username;
    private String password;
    public String body=null;
    private Gson_User user;
    private SharedPreferences sp;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        lottieAnimationView = findViewById(R.id.sp_lottieAnimationView);
        lottieAnimationView.loop(true);
        // 开始播放动画
        lottieAnimationView.playAnimation();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("autologin",false))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
        {
            username = sp.getString("account", "");
            password = sp.getString("password", "");
            post();
        } else {
            //跳到登录界面
            Intent intent4 = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent4);
        }
    }

    private void post()
    {
        new Thread(() -> {
            SystemClock.sleep(2000);//等待2秒钟
            String urlParam = "?" + "&password=" + password + "&username=" + username;
            Log.d("url",urlParam);
//            Headers headers = new Headers.Builder()
//                    .add("appId", Constant.APPID)
//                    .add("appSecret", Constant.APPSECRET)
//                    .add("Accept", "application/json, text/plain, */*")
//                    .build();
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .url("http://47.107.52.7:88/member/photo/user/login"+ urlParam)
                    // 将请求头加至请求中
                   // .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();

            try
            {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(logincallback);
            }
            catch (NetworkOnMainThreadException ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    private final Callback logincallback=new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException
        {
            ResponseBody responseBody = response.body();
            body = responseBody.string();
            Log.d("LOG - 登录", "响应体 : " + body);

            if (response.isSuccessful()) {
                runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Persons responseParse = gson.fromJson(body, Persons.class);
                    user = responseParse.getUser();
                    Message message = new Message();
                    switch (responseParse.getMsg()) {
                        case "登录成功":
                            message.arg1 = 1;
                            break;
                        case "密码错误":
                            message.arg1 = 2;
                            break;
                        case "当前登录用户不存在":
                            message.arg1 = 3;
                            break;
                        case "必传字段不能为空":
                            message.arg1 = 4;
                            break;
                        default:
                            message.arg1 = -1;
                            break;
                    }
                    mLoginHandler.sendMessage(message);
                });
            }
        }
    };

    private final Handler mLoginHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
                Intent intent3 = new Intent(SplashActivity.this, Persons.Menu_Activity.class);
                intent3.putExtra("userInfo",body);
                startActivity(intent3);
        }
    };
}
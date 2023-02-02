package iss.ad.team6.sharefood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import iss.ad.team6.sharefood.Gson.Constant;
import iss.ad.team6.sharefood.Gson.Gson_Base;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 这个类完成了注册
 */
public class SignupActivity extends AppCompatActivity
{
    private Button btn_signup;//注册按钮
    private EditText et_newAccount;//账号输入框
    private EditText et_newPassword;//密码输入框
    private String newAccount;//账号
    private String newPassword;//密码

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }
    private void initView()
    {
        et_newAccount=findViewById(R.id.et_newAccount);//账号输入框
        et_newPassword=findViewById(R.id.et_newpwd);//密码输入框
        btn_signup=findViewById(R.id.btn_signupnew);//注册按钮
        /**
         * 注册监听事件
         */
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAccount=et_newAccount.getText().toString();
                newPassword=et_newPassword.getText().toString();
                if(newAccount.equals("")||newPassword.equals(""))
                {
                    Toast.makeText(SignupActivity.this,"Account or password cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    post();
                }
            }
        });
    }
    private void post(){
        new Thread(() -> {

            // url路径
            String url = "https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/signup";
            Gson gson=new Gson();
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", Constant.APPID)
                    .add("appSecret", Constant.APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("password", newPassword);
            bodyMap.put("username", newAccount);
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(postcallback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback postcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            if(response.isSuccessful())
            {
                String responseData=response.body().string();
                Gson gson=new Gson();
                Gson_Base body=gson.fromJson(responseData,Gson_Base.class);
                if(body.getCode()==200)
                {
                    Looper.prepare();
                    Toast.makeText(SignupActivity.this,"Register Successfully！",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Looper.loop();
                }
                else if(body.getCode()==500)
                {
                    Looper.prepare();
                    Toast.makeText(SignupActivity.this,"This Account has been registered！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
            }

    };
}

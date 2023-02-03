package iss.ad.team6.sharefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import iss.ad.team6.sharefood.bean.LoginBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private final String loginUrl="https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/authenticate/login";
    private EditText edit_name,edit_psd;
    private CheckBox checkBox;
    private Button btn;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
        checkBox= (CheckBox) findViewById(R.id.login_recall);
        edit_name=(EditText) findViewById(R.id.name);
        edit_psd= (EditText) findViewById(R.id.password);
        btn=(Button)findViewById(R.id.btn_login) ;

        output();

        btn.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String account=edit_name.getText().toString().trim();
                String password=edit_psd.getText().toString().trim();

                if(account.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"user a/c or password cannot be blank",Toast.LENGTH_SHORT).show();

                } else {
                    login(account,password);

                }
            }
        }));

    }

    private void login(String account,String password){

        MediaType JSON=MediaType.parse("application/json;charset=utf-8");

        JSONObject jsJson=new JSONObject();
        try {
            jsJson.put("account", account);
            jsJson.put("password", password);
        } catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("1111111 jsJson ====",":" +jsJson);

        OkHttpClient client=new OkHttpClient();

        RequestBody requestBody=RequestBody.create(JSON,String.valueOf(jsJson));

        Request request=new Request.Builder()
                .url(loginUrl)
                .addHeader("appSecret","123456")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("111111 post Request failure=====","Message: "+e.getMessage());
                Log.d("111111 post Request failure=====","Request: "+call.request());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseData=response.body().string();

                InputStream responseStream=response.body().byteStream();

                Log.d("111111 post Request Success =====","Response Data: "+responseData);
                Log.d("111111 post Request Success =====","Response Stream: "+responseStream);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginBean bean=new Gson().fromJson(responseData,LoginBean.class);
                        Log.d("111111 Request Success =====","getAccess_token: "+bean.getData().getAccess_token());

                        pref=getSharedPreferences("loginsp",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("token",bean.getData().getAccess_token());
                        editor.commit();

                        Log.d("111111 main activity >>>","getAccess_token: "+pref.getString("token",""));

                        input();

                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);


                        startActivity(intent);

                        finish();
                    }
                });

            }
        });
    }

    private  void output(){
        pref=getSharedPreferences("mypsd",MODE_PRIVATE);
        String name1=pref.getString("name","");
        String psd1=pref.getString("psd","");
        boolean ischecked1=pref.getBoolean("isChecked",false);
        edit_name.setText(name1);
        edit_psd.setText(psd1);
        checkBox.setChecked(ischecked1);
    }

    private void input(){
        pref= getSharedPreferences("mypsd",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        if(checkBox.isChecked()){
            editor.putString("name",edit_name.getText().toString());
            editor.putString("psd",edit_psd.getText().toString());
            editor.putBoolean("isChecked",true);
        } else {
            editor.putString("name",edit_name.getText().toString());
            editor.putString("psd","");
            editor.putBoolean("isChecked",false);

        }
        editor.commit();
    }
}
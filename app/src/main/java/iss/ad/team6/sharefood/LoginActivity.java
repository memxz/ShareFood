package iss.ad.team6.sharefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

    private final String loginUrl="https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/individual/authenticate";//https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/authenticate/login";//https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/dummy/authenticate";

    private EditText edit_name,edit_psd;
    private CheckBox checkBox;
    private Button btn;
    private SharedPreferences pref;
    private ImageView img_del;
    private ImageView img_del2;
    private ImageView ivPwdSwitch;
    private Boolean bPwdSwitch=false;
    private TextView registerAccount;

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
        ivPwdSwitch=findViewById(R.id.iv_pwd_switch);//密码是否可见
        img_del = findViewById(R.id.iv_et_num_delete);//清空账号栏的EditText
        img_del2 = findViewById(R.id.iv_et_pwd_delete);//清空密码栏的EditText
        registerAccount = findViewById(R.id.registered_account);//注册按钮
        btn=(Button)findViewById(R.id.btn_login) ;

        output();

        /**
         * Listen to Signup Click event
         */
        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Jump to register
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Clear account/email editText event listener
         */
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_name.setText("");
            }
        });

        /**
         * Clear password row event listener
         */
        img_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_psd.setText("");
            }
        });


        /**
         * Listen to Hide/unhidden password event
         */
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPwdSwitch = !bPwdSwitch;
                if(bPwdSwitch)
                {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                    edit_psd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    //input password or common text
                    edit_psd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    //set up font format
                    edit_psd.setTypeface(Typeface.DEFAULT);
                }
            }
        });

        //Listen to Login btn Click event
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
            jsJson.put("email", account);
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
                            if(!TextUtils.isEmpty(responseData)){
                                LoginBean bean=new Gson().fromJson(responseData,LoginBean.class);
                                Log.d("111111 Request Success =====","getEmail: "+bean.getEmail());

                                pref=getSharedPreferences("loginsp",MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("token","123456");//bean.getData().getAccess_token());//Hardcode token since simple the entity
                                //newly add for detailActivity
                                editor.putString("userId",bean.getUserId().toString());
                                editor.putString("userName",bean.getUserName());
                                editor.commit();

                                Log.d("111111 main activity >>>","getAccess_token: "+pref.getString("token",""));

                                input();

                                Intent intent=new Intent(LoginActivity.this, Menu_Activity.class); //MainActivity.class); //now temp jump to main/menu activity
                                //newly add for detailActivity
                                intent.putExtra("userInfo",responseData);

                                startActivity(intent);

                                finish();
                            } else{
                                Toast.makeText(LoginActivity.this,
                                        "User account or Password Wrong！",
                                        Toast.LENGTH_SHORT).show();}
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
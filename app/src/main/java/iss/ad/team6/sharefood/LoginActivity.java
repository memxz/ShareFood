package iss.ad.team6.sharefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import iss.ad.team6.sharefood.bean.LoginBean;
import iss.ad.team6.sharefood.net.NetUtil;

public class LoginActivity extends AppCompatActivity {

    EditText mEtPassword,mEtName,mEtEmail;
    Button mBtnLogin;
    TextView mTvForgetPassword,mTvSignup;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void initEvent() {
//        mTvSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=mEtName.getText().toString();
                final String email = mEtEmail.getText().toString();
                final String password = mEtPassword.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("email",email);
                            jsonObject.put("password",password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String url = "/authenticateuser";

                        String data = NetUtil.getData(jsonObject, url);

                        Gson gson = new Gson();
                        final LoginBean loginBean = gson.fromJson(data, LoginBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(loginBean!=null){
                                    Intent intent = new Intent(LoginActivity.this,
                                            FoodListActivity.class);
                                    NetUtil.userName = loginBean.getUserName();

                                    //save to share preference
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Name, name);
                                    editor.putString(Email, email);
                                    editor.commit();

                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,
                                            "User Name or Password Wrong！",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }).start();
            }
        });
    }


    private void initView() {
        mEtName = findViewById(R.id.et_name);
        mEtPassword = findViewById(R.id.et_pass);
        mEtEmail = findViewById(R.id.et_email);
        mTvSignup = findViewById(R.id.tv_Signup);
        mTvForgetPassword = findViewById(R.id.tv_resAcc);
        mBtnLogin = findViewById(R.id.button);
    }
}
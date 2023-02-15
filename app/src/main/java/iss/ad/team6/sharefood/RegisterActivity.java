package iss.ad.team6.sharefood;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import iss.ad.team6.sharefood.bean.LoginBean;
import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.HttpUtil;
import iss.ad.team6.sharefood.utils.Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private TextView btnRegister,tvLogin;
    private EditText etUName,etPhone,etPassword,etEmail,etSalary;
    private RadioButton rb1;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);

        setContentView(R.layout.activity_register);
        etUName = findViewById(R.id.et_uName);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etSalary = findViewById(R.id.et_salary);
        rb1 = findViewById(R.id.rb_1);

        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);


        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    private void doRegister() {
        String userName = etUName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String salary = etSalary.getText().toString().trim();
        String role = rb1.isChecked() ? "giver" : "receiver";

        if (Utils.isEmptyStr(phone)) {
            showToast("invalid phone");
            return;
        }
        if (Utils.isEmptyStr(password) || password.length() < 6) {
            showToast("invalid password");
            return;
        }
        if (Utils.isEmptyStr(userName) || userName.length() < 8) {
            showToast("user name cannot be empty and length must be longer than 6");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("phone", phone.startsWith("+") ? phone : "+" + phone);
        params.put("email", email);
        params.put("salary", salary);
        params.put("role", role);
        HttpUtil.post_json(Api.api_register, params, new HttpUtil.OnGetDataCallback() {
            @Override
            public void onGetSuccess(String json) {
                Log.d(TAG, "onGetSuccess: json = " + json);
                //RegisterResultBean resp = new Gson().fromJson(json, RegisterResultBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null || json.length() == 0) {
                            showToast("register failed");
                            return;
                        }
                        LoginBean resp = new Gson().fromJson(json, LoginBean.class);
                        //if (resp.isSuccess()){
                        if (resp.getUserId() != null) {
                            showToast("register successfully");
                            finish();
                        } else {
                            showToast("failed registering");
                        }
                    }
                });
            }

            @Override
            public void onGetFailed(String msg) {
                Log.d(TAG, "onGetFailed: " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("failed");
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == btnRegister.getId()) {
            doRegister();
        } else if (vid == tvLogin.getId()) {
            finish();
        }
    }

    protected void showToast(String msg) {
        mToast.setText(msg);
        mToast.show();
    }
}


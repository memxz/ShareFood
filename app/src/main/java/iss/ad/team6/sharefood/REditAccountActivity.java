package iss.ad.team6.sharefood;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.BaseActivity;
import iss.ad.team6.sharefood.utils.HttpUtil;

public class REditAccountActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = REditAccountActivity.class.getSimpleName();
    private String userId;
    private TextView btnNext;
    private EditText etUName, etPassword;

    @Override
    protected void initView() {
        setContentView(R.layout.r_activity_edit_account);
        btnNext = findViewById(R.id.btn_next);
        etUName = findViewById(R.id.et_uName);
        etPassword = findViewById(R.id.et_password);
        btnNext.setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("loginsp", MODE_PRIVATE);
        userId = pref.getString("userId", "");
    }

    private void doNext() {
        String userName = etUName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (isEmptyStr(userName)) {
            showToast("user name cannot be empty");
            return;
        }
        if (isEmptyStr(password) || password.length() < 6) {
            showToast("invalid password");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("userId", userId);
        HttpUtil.post_json(Api.api_edit_account, params, new HttpUtil.OnGetDataCallback() {
            @Override
            public void onGetSuccess(String json) {
                Log.d(TAG, "onGetSuccess: json = " + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("edit successfully");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            @Override
            public void onGetFailed(String msg) {
                Log.d(TAG, "onGetFailed: " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(msg);
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == btnNext.getId()) {
            doNext();
        }
    }

}


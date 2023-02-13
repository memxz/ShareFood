package iss.ad.team6.sharefood;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import iss.ad.team6.sharefood.base.BaseActivity;
import iss.ad.team6.sharefood.bean.LoginBean;
import iss.ad.team6.sharefood.databinding.ActivityEditAccountBinding;
import iss.ad.team6.sharefood.databinding.ActivityRegisterBinding;
import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditAccountActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG =EditAccountActivity.class.getSimpleName();
    private ActivityEditAccountBinding mBinding;
    private String userId;

    @Override
    protected void initView() {
        mBinding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.btnNext.setOnClickListener(this);

        SharedPreferences pref=getSharedPreferences("loginsp",MODE_PRIVATE);
        userId = pref.getString("userId","");
    }

    private void doNext(){
        String userName = mBinding.etUName.getText().toString().trim();
        String password = mBinding.etPassword.getText().toString().trim();
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
                Log.d(TAG, "onGetSuccess: json = "+json);
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
                Log.d(TAG, "onGetFailed: "+msg);
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
        if (vid ==  mBinding.btnNext.getId()){
            doNext();
        }
    }

}


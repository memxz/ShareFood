package iss.ad.team6.sharefood;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import iss.ad.team6.sharefood.base.BaseActivity;
import iss.ad.team6.sharefood.bean.RegisterResultBean;
import iss.ad.team6.sharefood.databinding.ActivityRegisterBinding;
import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.HttpUtil;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private ActivityRegisterBinding mBinding;

    @Override
    protected void initView() {
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.btnRegister.setOnClickListener(this);
        mBinding.tvLogin.setOnClickListener(this);
    }

    private void doRegister(){
        String userName = mBinding.etUName.getText().toString().trim();
        String phone = mBinding.etPhone.getText().toString().trim();
        String password = mBinding.etPassword.getText().toString().trim();
        String email=mBinding.etEmail.getText().toString().trim();
        String salary=mBinding.etSalary.getText().toString().trim();
        String role = mBinding.rb1.isChecked()?"seller":"buyer";

        if (isEmptyStr(phone)) {
            showToast("invalid phone");
            return;
        }
        if (isEmptyStr(password) || password.length() < 6) {
            showToast("invalid password");
            return;
        }
        if (isEmptyStr(userName)) {
            showToast("user name cannot be empty");
            return;
        }

        String url = Api.baseUrl + Api.api_register;
        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("pwd", password);
        params.put("phone", phone);
        params.put("email", email);
        params.put("salary", salary);
        params.put("role", role);
        HttpUtil.post(url, params, new HttpUtil.OnGetDataCallback() {
            @Override
            public void onGetSuccess(String json) {
                Log.d(TAG, "onGetSuccess: json = "+json);
                RegisterResultBean resp = new Gson().fromJson(json, RegisterResultBean.class);
                if (resp.isSuccess()){
                    showToast("register successfully");
                    finish();
                }else {
                    showToast("failed registering");
                }
            }
            @Override
            public void onGetFailed(String msg) {
                Log.d(TAG, "onGetFailed: "+msg);
                showToast("failed");
            }
        });
    }


    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid ==  mBinding.btnRegister.getId()){
            doRegister();
        }
        else if (vid == mBinding.tvLogin.getId()) {
            finish();
        }
    }
}


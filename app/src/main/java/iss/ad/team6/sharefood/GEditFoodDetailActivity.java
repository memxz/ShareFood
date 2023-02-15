package iss.ad.team6.sharefood;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import iss.ad.team6.sharefood.base.BaseActivity;
import iss.ad.team6.sharefood.bean.FoodBean;
import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.HttpUtil;

public class GEditFoodDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = GEditFoodDetailActivity.class.getSimpleName();
    private String userId;
    private TextView btnNext;
    private EditText etTitle, etDetail;

    private FoodBean data;

    @Override
    protected void initView() {
        data = (FoodBean) getIntent().getSerializableExtra("DATA");
        setContentView(R.layout.g_activity_edit_food_detail);
        btnNext = findViewById(R.id.btn_next);
        etTitle = findViewById(R.id.et_title);
        etDetail = findViewById(R.id.et_detail);
        btnNext.setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("loginsp", MODE_PRIVATE);
        userId = pref.getString("userId", "");

        etTitle.setText(data.getTitle());
        etDetail.setText(data.getDescription());
    }

    private void doNext() {
        String title = etTitle.getText().toString().trim();
        String detail = etDetail.getText().toString().trim();
        if (isEmptyStr(title)) {
            showToast("food title cannot be empty");
            return;
        }
        if (isEmptyStr(detail)) {
            showToast("food detail cannot be empty");
            return;
        }
        data.setTitle(title);
        data.setDescription(detail);

        HttpUtil.put_json(Api.api_edit_food, data, new HttpUtil.OnGetDataCallback() {
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


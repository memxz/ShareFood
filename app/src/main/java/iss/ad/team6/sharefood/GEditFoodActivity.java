package iss.ad.team6.sharefood;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iss.ad.team6.sharefood.Adapter.HistoryAdapter;
import iss.ad.team6.sharefood.base.BaseActivity;
import iss.ad.team6.sharefood.bean.FoodBean;
import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.HttpUtil;

public class GEditFoodActivity extends BaseActivity {

    private final String TAG = GEditFoodActivity.class.getSimpleName();

    private String userId;

    private ListView lv_list;

    private HistoryAdapter adapter;


    @Override
    protected void initView() {
        SharedPreferences pref = getSharedPreferences("loginsp", MODE_PRIVATE);
        userId = pref.getString("userId", "");

        setContentView(R.layout.g_activity_edit_food);
        lv_list = findViewById(R.id.lv_list);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodBean item = adapter.getItem(i);
                Intent intent = new Intent(GEditFoodActivity.this, GEditFoodDetailActivity.class);
                intent.putExtra("DATA",item);
                startActivityForResult(intent, 200);
            }
        });
        adapter = new HistoryAdapter();
        lv_list.setAdapter(adapter);

        getDatas();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                getDatas();
                break;
        }

    }

    private void showDatas(List<FoodBean> datas) {
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
    }

    private void getDatas() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        HttpUtil.get2(Api.api_all_uncollected, params, new HttpUtil.OnGetDataCallback() {
            @Override
            public void onGetSuccess(String json) {
                Log.d(TAG, "onGetSuccess: json = " + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null || json.length() == 0) {
                            showToast("cannot find list/unlist food items");
                            return;
                        }
                        if (json.contains("404")) {
                            showToast("404 error");
                            return;
                        }
                        List<FoodBean> datas = new Gson().fromJson(json, new TypeToken<List<FoodBean>>() {
                        }.getType());
                        showDatas(datas);
                    }
                });
            }

            @Override
            public void onGetFailed(String msg) {
                Log.d(TAG, "onGetFailed: " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("failed loading history orders");
                    }
                });
            }
        });

    }
}

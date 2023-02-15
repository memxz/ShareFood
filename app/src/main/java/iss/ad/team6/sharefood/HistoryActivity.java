package iss.ad.team6.sharefood;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iss.ad.team6.sharefood.adapter.HistoryAdapter;
import iss.ad.team6.sharefood.bean.FoodBean;
import iss.ad.team6.sharefood.utils.Api;
import iss.ad.team6.sharefood.utils.BaseActivity;
import iss.ad.team6.sharefood.utils.HttpUtil;

public class HistoryActivity extends BaseActivity {

    private final String TAG = HistoryActivity.class.getSimpleName();

    private String userId;

    private ListView lv_list;

    private HistoryAdapter adapter;


    @Override
    protected void initView() {
        SharedPreferences pref = getSharedPreferences("loginsp", MODE_PRIVATE);
        userId = pref.getString("userId", "");

        setContentView(R.layout.activity_history);
        lv_list = findViewById(R.id.lv_list);
        adapter = new HistoryAdapter();
        lv_list.setAdapter(adapter);

        getDatas();
    }

    private void showDatas(List<FoodBean> datas) {
        List<FoodBean> filter_datas = new ArrayList<>();
        {
            if (datas != null) {
                for (FoodBean data : datas) {
                    if (data.isCollected()) {
                        filter_datas.add(data);
                    }
                }
            }
        }
        adapter.setDatas(filter_datas);
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
    }

    private void getDatas() {
        Map<String, String> params = new HashMap<>();
        //params.put("isCollected", "true");
        params.put("userId", userId);
        HttpUtil.get2(Api.api_history, params, new HttpUtil.OnGetDataCallback() {
            @Override
            public void onGetSuccess(String json) {
                Log.d(TAG, "onGetSuccess: json = " + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null || json.length() == 0) {
                            showToast("cannot load history orders");
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

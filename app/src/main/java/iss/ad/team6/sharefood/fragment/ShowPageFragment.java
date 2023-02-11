package iss.ad.team6.sharefood.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import iss.ad.team6.sharefood.Adapter.FoodAdapter;
import iss.ad.team6.sharefood.FoodDetailActivity;
import iss.ad.team6.sharefood.R;
import iss.ad.team6.sharefood.bean.FoodBean;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowPageFragment extends Fragment implements AdapterView.OnItemClickListener {

    public final String foodlistUrl ="https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/get-list";//https://v99xcpwju4.execute-api.ap-northeast-1.amazonaws.com/FoodDelieveryTest/getfoodlist/";
    public final String searchUrl="https://v99xcpwju4.execute-api.ap-northeast-1.amazonaws.com/FoodDelieveryTest/getsearchlist/";
    ListView foodListView;
    List<FoodBean> foodList;
    List<FoodBean>selectedList;
    RadioGroup radioGroup;
    RadioButton rb;
    final int cSelectedListMaxSize = 20;
    public ShowPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_page, container, false);
        foodListView=view.findViewById(R.id.foodList);
        Button searchBtn=view.findViewById(R.id.searchBtn);
        if(searchBtn!=null)
        {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OkHttpHandler selectHandler=new OkHttpHandler();
                    EditText input=view.findViewById(R.id.search);
                    String content=input.getText().toString();
                    String halaStatus=" ";
                    radioGroup=view.findViewById(R.id.radioGroup);
                    for(int i=0;i<radioGroup.getChildCount();i++)
                    {
                        RadioButton rb=(RadioButton) radioGroup.getChildAt(i);
                        if(rb.isChecked())
                        {
                            halaStatus=rb.getText().toString();
                        }
                    }
                    Gson post=new Gson();
                    Map<String,String> inputMap=new HashMap<String,String>();
                    inputMap.put("Status",halaStatus);
                    inputMap.put("search",content);
                    String JsonStr=post.toJson(inputMap);
                    String method = "POST";
                    selectHandler.execute(searchUrl, method, JsonStr);
                }
            });
        }

        Button refreshButton=view.findViewById(R.id.refreshButton);
        if(refreshButton != null)
        {
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshFoodList();
                }
            });
        }

        OkHttpHandler defaultHandler=new OkHttpHandler();

        // For test
        //final String userID = "9";
        SharedPreferences pref = this.getActivity().getSharedPreferences("loginsp", MODE_PRIVATE);
        final String userID=pref.getString("userId","");

        Map<String,String> inputMap=new HashMap<String,String>();
        inputMap.put("userID",userID);
        Gson gson = new Gson();
        final String json = gson.toJson(inputMap);
        String method = "GET";
        defaultHandler.execute(foodlistUrl, method, json);
        return view;
    }
    public class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();


        @Override
        protected Object doInBackground(Object[] params) {


            Request.Builder builder = new Request.Builder();
            builder.url((String) params[0]);
            String method = (String) params[1];
            if(method == "GET")
            {
                // Do nothing
            }
            else if(method == "POST")
            {
                String json = (String)params[2];
                builder.post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), json));
            }

            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String result= (String) o;
            Gson gson=new Gson();

            foodList=gson.fromJson(result,new TypeToken<List<FoodBean>>()
            {}.getType());

            refreshFoodList();
        }

    }

    public static final String SELECTED_FOOD = "selectedFood";

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FoodBean foodInfo = selectedList.get(i);
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        //intent.putExtra(SELECTED_FOOD, foodInfo);
        //Add Extra info
        intent.putExtra("puserId", foodInfo.getPublisher().getUserId().toString());
        intent.putExtra("foodId", foodInfo.getFoodId().toString());
        startActivity(intent);
    }

    private void refreshFoodList()
    {
        Collections.shuffle(foodList);

        selectedList = foodList.subList(0, Math.min(cSelectedListMaxSize, foodList.size()));

        FoodAdapter adapter=new FoodAdapter(ShowPageFragment.this,selectedList);
        foodListView.setAdapter(adapter);
        foodListView.setOnItemClickListener(ShowPageFragment.this);
    }




}
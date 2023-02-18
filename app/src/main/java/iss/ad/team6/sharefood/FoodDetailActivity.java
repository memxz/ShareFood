package iss.ad.team6.sharefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import iss.ad.team6.sharefood.bean.FoodBean;
import iss.ad.team6.sharefood.bean.FoodType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;

/**
 * Click image to view detail
 */
public class FoodDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String userId;
    private String shareId;
    private String userHeadimg;
    private String likeId;
    private String collectId;
    private TextView tv_title;
    private PhotoView iv_picture;
    private TextView tv_content;
    private TextView tv_username;
    private TextView tv_comment;
    private ImageView iv_like;
    private String title;
    private String username;
    private String content;
    private String imageUrl;
    private boolean islike = false;
    private boolean isCollect = false;
    private ImageView iv_collect;
    private TextView tv_listDays;
    private TextView tv_foodType;
    private TextView tv_available;
    Bitmap bitmap;
    private String pl_username;
    private String puserid;
    private String foodId;
    private FoodBean responseData;
    private String available;
    private String listDays;
    private String foodType;
    private Button requestButton;
    private Button completeButton;
    private Button cancelReqButton;
    private LatLng pickLocation;// = new LatLng(1.3742107305278901, 103.76726999611022);
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
            @Override
            public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
                //println(it.name)
                Log.d("TAG", "onMapsSdkInitialized: ");
            }
        });
        setContentView(R.layout.activity_fooddetail);
        SharedPreferences pref = getSharedPreferences("loginsp", MODE_PRIVATE);
        userId=pref.getString("userId","");

        Intent intent = getIntent();
        //Get value From  List_Fragment
//        userId = intent.getStringExtra("userId");
        foodId = intent.getStringExtra("foodId");
//        shareId = intent.getStringExtra("shareId");
//        userHeadimg = intent.getStringExtra("userHeadimg");
//        pl_username = intent.getStringExtra("discover_username");
        initView();
        get();
        initData();
    }

    private void initView() {
        /**
         * Hide status
         */
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //Title
        tv_title = findViewById(R.id.tv_photo_details_title);
        //food image
        iv_picture = findViewById(R.id.iv_photo_details_background);
        //Description
        tv_content = findViewById(R.id.tv_photo_details_context);
        //Publisher user name
        tv_username = findViewById(R.id.tv_photo_details_username);
        // Food availability
        tv_available = findViewById(R.id.available);
        // Food list days
        tv_listDays = findViewById(R.id.listDay);
        // food type(halal/non-halal)
        tv_foodType = findViewById(R.id.foodType);

        requestButton = (Button) findViewById(R.id.requestBtn);
        cancelReqButton=findViewById(R.id.cancelReqBtn);
        completeButton = (Button) findViewById(R.id.completeBtn);
        requestButton.setVisibility(View.GONE);
        cancelReqButton.setVisibility(View.GONE);
        completeButton.setVisibility(View.GONE);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       /* //Comment
        tv_comment = findViewById(R.id.jump_test);
        //Like Heart symbol
        iv_like = findViewById(R.id.iv_photo_details_like);
        //store/collect star symbol
        iv_collect = findViewById(R.id.iv_photo_details_collect);*/
    }

    private void initData() {

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiserequestPost();
            }
        });
        cancelReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelrequestPost();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completerequestPost();
            }
        });
    }
    //Raise Request Post
    private void raiserequestPost() {
        new Thread(() -> {

            // url
            String url = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/update";//https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/getFooddetail/raiserequest";

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            //Set Pending to false to cancel request
            responseData.setPendingPickup(true);
            responseData.setRequestId(Long.parseLong(userId));
            String json=new Gson().toJson(responseData);
            //request
            Request request = new Request.Builder()
                    .url(url)
                    // add header
                    .addHeader("appSecret", "123456")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .put(RequestBody.create(MEDIA_TYPE_JSON, json))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //Raise post request，get callback
                client.newCall(request).enqueue(Getcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    //cancel Request Post
    private void cancelrequestPost() {
        new Thread(() -> {

            // url
            String url = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/update";//https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/getFooddetail/cancelrequest";

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            //Set Pending to false to cancel request
            responseData.setPendingPickup(false);
            responseData.setRequestId(null);
            String json=new Gson().toJson(responseData);
            //request
            Request request = new Request.Builder()
                    .url(url)
                    // add header
                    .addHeader("appSecret", "123456")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .put(RequestBody.create(MEDIA_TYPE_JSON, json))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //Raise post request，get callback
                client.newCall(request).enqueue(Getcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    //complete Request Post
    private void completerequestPost() {
        new Thread(() -> {

            // url
            String url = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/update";//https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/getFooddetail/completerequest";

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            //Set Pending to false to cancel request
            responseData.setPendingPickup(false);
            responseData.setCollected(true);
            String json=new Gson().toJson(responseData);
            //request
            Request request = new Request.Builder()
                    .url(url)
                    // add header
                    .addHeader("appSecret", "123456")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .put(RequestBody.create(MEDIA_TYPE_JSON, json))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //Raise post request，get callback
                client.newCall(request).enqueue(Getcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Callback
     */
    /*private final Callback requestPostcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {

            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO request success
            if (response.isSuccessful()) {
                String responseBody= response.body().string();
                Log.d("FoodDetailsActivity_Data", responseBody);
                Gson gson = new Gson();
                responseData = gson.fromJson(responseBody, FoodBean.class);
                requestButton.setVisibility(View.GONE);
                cancelReqButton.setVisibility(View.VISIBLE);
                completeButton.setVisibility(View.GONE);

            }
        }
    };*/




    /**
     * Get shared food detail
     */
    private void get() {
        new Thread(() -> {
            // url
            String url = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food\n";//https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/getFooddetail\n?";
            String info = "/"+ foodId;

            //Request
            Request request = new Request.Builder()
                    .url(url + info)
                    // add header
                    .addHeader("appSecret", "123456")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //Send Request，& callback
                client.newCall(request).enqueue(Getcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * callback
     */
    private final Callback Getcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {

            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //Process request
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("FoodDetailsActivity_Data", responseBody);
                Gson gson = new Gson();
                responseData = gson.fromJson(responseBody, FoodBean.class);

                //Get image url
                imageUrl = responseData.getImgUrl();
                //puserid = responseData.getPublisher().getUserId().toString();
                Message message = new Message();
                message.obj = imageUrl;
                setPictureHandler.sendMessage(message);
            }
        }
    };

    private final Handler setPictureHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            updateDetailInfo(msg);
        }
    };

    private void updateDetailInfo(Message msg){
        title = responseData.getTitle();//Title
        content = responseData.getDescription();//Description
        username = responseData.getPublisher().getUserName();//publisher
        puserid=responseData.getPublisher().getUserId().toString();//publisher
        requestId=responseData.getRequestId()==null?null:responseData.getRequestId().toString();
        if(responseData.isPendingPickup() && !responseData.isCollected() && responseData.isListed()){
            available="Blocked & Pending for pick-up";
            if(puserid!=null && puserid!="0" && !puserid.equals(userId) && userId.equals(requestId)) {
                requestButton.setVisibility(View.GONE);
                cancelReqButton.setVisibility(View.VISIBLE);
                completeButton.setVisibility(View.VISIBLE);
            }
            else {
                requestButton.setVisibility(View.GONE);
                cancelReqButton.setVisibility(View.GONE);
                completeButton.setVisibility(View.GONE);
            }
            Log.d("111111==PendingPickup==Collected==Listed","true==false==true");
        }
        else if(!responseData.isPendingPickup() && responseData.isCollected() && responseData.isListed()){
            available="Food Already Collected";
            requestButton.setVisibility(View.GONE);
            cancelReqButton.setVisibility(View.GONE);
            completeButton.setVisibility(View.GONE);
            Log.d("111111==PendingPickup==Collected==Listed","false==true==true");

        }
        else if(!responseData.isPendingPickup() && !responseData.isCollected() && responseData.isListed() && !puserid.equals(userId)){
            available="Grab this";
            requestButton.setVisibility(View.VISIBLE);
            cancelReqButton.setVisibility(View.GONE);
            completeButton.setVisibility(View.GONE);
            Log.d("111111==PendingPickup==Collected==Listed","false==false==true");
        }else {
            available="Not Available";
            requestButton.setVisibility(View.GONE);
            cancelReqButton.setVisibility(View.GONE);
            completeButton.setVisibility(View.GONE);
            Log.d("111111==PendingPickup==Collected==Listed","??==??==false");
        }
        listDays= String.valueOf(responseData.getListDays());
        foodType=responseData.getFoodType().name().toString();
        tv_title.setText(title);
        tv_username.setText(username);
        tv_content.setText(content);
        tv_available.setText(available);
        tv_listDays.setText(listDays);
        tv_foodType.setText(foodType);
        //Fill image into view
        Glide.with(FoodDetailActivity.this).load(msg.obj).into(iv_picture);

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("11111responseData",responseData.getLatitude()+"-"+responseData.getLongitude());
        pickLocation= new LatLng(responseData.getLatitude(),responseData.getLongitude());
        //pickLocation= new LatLng(1.3742107305278901, 103.76726999611022);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pickLocation, 18));
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(SFO,14));
        map.addMarker(new MarkerOptions().position(pickLocation).title("Pick-up Point"));
    }
}
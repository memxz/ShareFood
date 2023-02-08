package iss.ad.team6.sharefood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

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
public class FoodDetailActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetail);
        Intent intent = getIntent();
        //Get value From  List_Fragment
        userId = intent.getStringExtra("userId");
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


       /* //Comment
        tv_comment = findViewById(R.id.jump_test);
        //Like Heart symbol
        iv_like = findViewById(R.id.iv_photo_details_like);
        //store/collect star symbol
        iv_collect = findViewById(R.id.iv_photo_details_collect);*/
    }

    private void initData() {
       /* //   Comment click event
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PhotoDetailsActivity.this, CommentActivity.class);
                intent.putExtra("shareId", shareId);
                intent.putExtra("userHeadimg", userHeadimg);
                intent.putExtra("userId", userId);
                intent.putExtra("pl_username", pl_username);
                startActivity(intent);
            }
        });

        // Like click event
        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                islike = !islike;
                if (islike) {
                    iv_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                    likePost();
                } else {
                    iv_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    unlikePost();
                }

                //Animation setup
                iv_like.startAnimation(AnimationUtils.loadAnimation(
                        PhotoDetailsActivity.this, R.anim.dianzan_anim));
            }
        });
        //  Store/collect click event
        iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollect = !isCollect;
                if (isCollect) {
                    iv_collect.setImageResource(R.drawable.ic_baseline_star_24);
                    CollectPost();
                } else {
                    iv_collect.setImageResource(R.drawable.ic_baseline_collect_border_white);
                    unCollectPost();
                }
                //animation setup
                iv_collect.startAnimation(AnimationUtils.loadAnimation(
                        FoodDetailActivity.this, R.anim.dianzan_anim));
            }
        });*/
    }

        /**
         * Get shared food detail
         */
        private void get() {
            new Thread(() -> {
                // url
                String url = "https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/getFooddetail\n?";
                String info = "foodId=12";// + foodId;

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
                title = responseData.getTitle();//Title
                content = responseData.getDescription();//Description
                username = responseData.getPublisher().getUserName();//publisher
                if(responseData.isPendingPickup() && !responseData.isCollected() && responseData.isListed()){
                    available="Blocked & Pending for pick-up";
                    Log.d("111111==PendingPickup==Collected==Listed","true==false==true");
                }
                else if(!responseData.isPendingPickup() && responseData.isCollected() && responseData.isListed()){
                    available="Food Already Collected";
                    Log.d("111111==PendingPickup==Collected==Listed","false==true==true");

                }
                else if(!responseData.isPendingPickup() && !responseData.isCollected() && responseData.isListed()){
                    available="Grab this";
                    Log.d("111111==PendingPickup==Collected==Listed","false==false==true");
                }else {
                    available="Not Available";
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
        };

}
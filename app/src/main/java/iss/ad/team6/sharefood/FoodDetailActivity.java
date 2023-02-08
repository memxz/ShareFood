package iss.ad.team6.sharefood;

import static com.example.pictureshare2.Gson.Constant.APPID;
import static com.example.pictureshare2.Gson.Constant.APPSECRET;

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
import com.example.pictureshare2.Gson.Constant;
import com.example.pictureshare2.Gson.Gson_Base;
import com.example.pictureshare2.Gson.Gson_Photo;
import com.example.pictureshare2.Gson.Gson_PhotoDetails;
import com.google.gson.Gson;

import java.io.IOException;

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
 * 点击图片的详情页
 */
public class FoodDetailActivity extends AppCompatActivity  {
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
    Gson_Photo photodetails;
    private boolean islike = false;
    private boolean isCollect = false;
    private ImageView iv_collect;
    Bitmap bitmap;
    private String pl_username;
    private String puserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        Intent intent = getIntent();
        //从  PhotoDiscoveryFragment这个类里面取值过来
        userId = intent.getStringExtra("userId");
        shareId = intent.getStringExtra("shareId");
        userHeadimg = intent.getStringExtra("userHeadimg");
        pl_username = intent.getStringExtra("discover_username");
        initView();
        get();
        initData();
    }

    private void initView() {
        /**
         * 隐藏状态栏
         */
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //标题
        tv_title = findViewById(R.id.tv_photo_details_title);
        //用户分享的图片
        iv_picture = findViewById(R.id.iv_photo_details_background);
        //用户分享的内容
        tv_content = findViewById(R.id.tv_photo_details_context);
        //左上角的用户username
        tv_username = findViewById(R.id.tv_photo_details_username);

//        tv_username.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(PhotoDetailsActivity.this, JumpAbout.class);
//                intent.putExtra("username", username);
//                intent.putExtra("userId", puserid);
//                startActivity(intent);
//            }
//        });
        //查看评论
        tv_comment = findViewById(R.id.jump_test);
        //喜欢的爱心图标
        iv_like = findViewById(R.id.iv_photo_details_like);
        //收藏的五角星图标
        iv_collect = findViewById(R.id.iv_photo_details_collect);
    }

    private void initData() {
//      评论的点击事件
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

//       点赞的点击事件
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

                //设置动画
                iv_like.startAnimation(AnimationUtils.loadAnimation(
                        PhotoDetailsActivity.this, R.anim.dianzan_anim));
            }
        });
//        收藏点击事件
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
                //设置动画
                iv_collect.startAnimation(AnimationUtils.loadAnimation(
                        PhotoDetailsActivity.this, R.anim.dianzan_anim));
            }
        });
    }

    /**
     * 获取单个图文分享的详情
     */
    private void get() {
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/share/detail?";
            String info = "shareId=" + shareId + "&userId=" + userId;
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", Constant.APPID)
                    .add("appSecret", Constant.APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();
            //请求组合创建
            Request request = new Request.Builder()
                    .url(url + info)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(Getcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback Getcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("PhotoDetailsActivity_数据", responseBody);
                Gson gson = new Gson();
                Gson_PhotoDetails responseData = gson.fromJson(responseBody, Gson_PhotoDetails.class);
                photodetails = responseData.getData();
                //获取图片的网址
                imageUrl = photodetails.getImageUrlList()[0];
                puserid = photodetails.getPUserId();
                Message message = new Message();
                message.obj = imageUrl;
                setPictureHandler.sendMessage(message);
            }
        }
    };

    //    private final Handler photoHandler = new Handler(Looper.getMainLooper())
    private final Handler setPictureHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            title = photodetails.getTitle();//标题
            content = photodetails.getContent();//内容
            username = photodetails.getUsername();//用户名字
            tv_title.setText(title);
            tv_username.setText(username);
            tv_content.setText(content);
            likeId = photodetails.getLikeId();//当前图片分享的用户点赞的主键id
            collectId = photodetails.getCollectId();//当前图片分享的用户收藏的主键id
            //把图片填入ohotoview
            Glide.with(PhotoDetailsActivity.this).load(msg.obj).into(iv_picture);

            islike = photodetails.getHasLike();//是否已点赞
            isCollect = photodetails.getHasCollect();//是否已收藏

            if (islike == true) {
                iv_like.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
            if (islike == false) {
                iv_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
            if (isCollect == true) {
                iv_collect.setImageResource(R.drawable.ic_baseline_star_24);
            }
            if (isCollect == false) {
                iv_collect.setImageResource(R.drawable.ic_baseline_collect_border_white);
            }

        }
    };

    private void likePost() {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/like?shareId=" + shareId + "&userId=" + userId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", Constant.APPID)
                    .add("appSecret", Constant.APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(likepostcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback likepostcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Gson_Base base = gson.fromJson(responseData, Gson_Base.class);
                if (base.getCode() == 200) {

                }

            }
        }
    };

    private void unlikePost() {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/like/cancel?likeId=" + likeId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", Constant.APPID)
                    .add("appSecret", Constant.APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(unlikePostcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback unlikePostcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Gson_Base base = gson.fromJson(responseData, Gson_Base.class);
                if (base.getCode() == 200) {

                }
            }
        }
    };

    private void CollectPost() {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/collect?shareId=" + shareId + "&userId=" + userId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", APPID)
                    .add("appSecret", APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(Collectcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback Collectcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                String responseData = response.body().string();
                Gson_Base body = gson.fromJson(responseData, Gson_Base.class);
                if (body.getCode() == 200) {
                    Log.d("收藏信息", "已收藏");
                }
            }
        }
    };

    private void unCollectPost() {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/collect/cancel?collectId=" + collectId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", APPID)
                    .add("appSecret", APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(unCollectcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback unCollectcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Gson_Base body = gson.fromJson(responseData, Gson_Base.class);
                if (body.getCode() == 200) {

                }
            }
        }
    };
}
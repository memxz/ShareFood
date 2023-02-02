package iss.ad.team6.sharefood.Gson;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import iss.ad.team6.sharefood.R;
import iss.ad.team6.sharefood.databinding.ActivityMenuBinding;

public class Persons
{
    @SerializedName("code")
    private int code;
    public int getCode() { return code; }

    @SerializedName("msg")
    private String msg;
    public String getMsg() { return msg; }

    @SerializedName("data")
    private Gson_User user;
    public Gson_User getUser() { return user; }

    public static class Menu_Activity extends AppCompatActivity
    {
        /**
         * 视图绑定类 对象
         */
        private ActivityMenuBinding binding;

        private String userId=null;
        private String username=null;
        private String userInfo=null;
        private String userHeadimg;

        public String getUserInfo()
        {
            return userInfo;
        }

        public String getUserId()
        {
            return userId;
        }
        public String getUserName()
        {
            return username;
        }
        public String getUserHeadimg()
        {
            return userHeadimg;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent intent=getIntent();
            userInfo=intent.getStringExtra("userInfo");
            setUserData();
            // 1 . 获取视图绑定类
            binding = ActivityMenuBinding.inflate(getLayoutInflater());
            // 2 . 关联视图绑定类 与 Activity
            setContentView(binding.getRoot());
            BottomNavigationView navView = findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_photodiscovery,R.id.navigation_home,R.id.navigation_myshare).build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu);
    //        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

        }

        private void setUserData()
        {
            Gson gson=new Gson();
            Persons userData=gson.fromJson(userInfo,Persons.class);
            Gson_User user=userData.getUser();
            userHeadimg=user.getAvatar();
            userId=user.getId();
            username=user.getUsername();
        }
    }
}



//{
//        msg:"string"
//        code:0
//        data:{
//        appKey:"string"
//        avatar:"string"
//        createTime:0
//        id:0
//        introduce:"string"
//        lastUpdateTime:0
//        password:"string"
//        sex:0
//        username:"string"
//        }
//        }

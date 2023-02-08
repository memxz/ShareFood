package iss.ad.team6.sharefood;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import iss.ad.team6.sharefood.Discovery.ListFragment;
import iss.ad.team6.sharefood.bean.LoginBean;
import iss.ad.team6.sharefood.databinding.ActivityMenuBinding;

public class Menu_Activity extends AppCompatActivity implements
        ListFragment.IListFragment
{
    /**
     * view binding class
     */
    private ActivityMenuBinding binding;

    private String userId=null;
    private String username=null;
    private String userInfo=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        userInfo=intent.getStringExtra("userInfo");
        setUserData();
        // 1 . get view binding class
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        // 2 . Link view binding class with Activity
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_photodiscovery).build();//R.id.navigation_home,R.id.navigation_myshare).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void setUserData()
    {
        Gson gson=new Gson();
        LoginBean userData=gson.fromJson(userInfo, LoginBean.class);
        //userHeadimg=user.getAvatar();
        userId=userData.getUserId().toString();
        username=userData.getUserName();
    }

    @Override
    public void viewDetail(int itemId) {
        startDetailActivity(itemId);
    }

    void startDetailActivity(int newItemId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("itemId", newItemId);
        startActivity(intent);
    }
}
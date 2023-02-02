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

public class Persons {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    @SerializedName("data")
    private Gson_User user;

    public Gson_User getUser() {
        return user;
    }
}

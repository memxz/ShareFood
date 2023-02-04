package iss.team6.sharefood.foodshare.base;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.foodshare.R;

public abstract class BaseActivity extends AppCompatActivity {
    private Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast=Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
        setStatusTextColor(true);
        initView();
    }
    protected abstract void initView();
    protected void setStatusTextColor(boolean isDarkTextColor){
        if (isDarkTextColor) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
    protected void showToast(String msg){
        mToast.setText(msg);
        mToast.show();
    }

    protected boolean isNotEmptyStr(String str){
        return !TextUtils.isEmpty(str);
    }
    protected boolean isEmptyStr(String str){
        return TextUtils.isEmpty(str);
    }
    protected void goActivity(Context context, Class clazz){
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }
}
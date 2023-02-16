package iss.ad.team6.sharefood;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import iss.ad.team6.sharefood.fragment.AddFragment;
import iss.ad.team6.sharefood.fragment.ManageAccountFragment;
import iss.ad.team6.sharefood.fragment.ShowPageFragment;
import iss.ad.team6.sharefood.utils.Utils;
import iss.ad.team6.sharefood.utils.tabhost.FragmentTabHost;
import iss.ad.team6.sharefood.utils.tabhost.OnTabActionListener;
import iss.ad.team6.sharefood.utils.tabhost.TabHost;
import iss.ad.team6.sharefood.utils.tabhost.TabWidget;

public class MainActivity extends AppCompatActivity implements OnTabActionListener {
    public static String[] MAIN_MENU = {"Home", "Add Food","Manage Account"};
    private FragmentTabHost fragment_tab_host;
    private TabWidget tabWidget;
    //点击图片数组
    private int[] menu_on_ids = {R.mipmap.icon_home, R.mipmap.icon_add, R.mipmap.icon_me,};
    //默认图片数组
    private int[] menu_nol_ids = {R.mipmap.icon_home, R.mipmap.icon_add, R.mipmap.icon_me,};

    private String[] menu_str_arr;
    private
    @ColorInt int menu_on_color, menu_nol_color;
    private int lastPosition = 0;
    private int nowPosition = 0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initViews();
        }

    private void initViews(){
        fragment_tab_host = findViewById(android.R.id.tabhost);
        fragment_tab_host.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabWidget = fragment_tab_host.getTabWidget();
        tabWidget.setOnTabActionListener(this);

        //设置字体的状态选择器
        menu_str_arr = MAIN_MENU;
        menu_on_color = getResources().getColor(R.color.purple_500);
        menu_nol_color = getResources().getColor(R.color.border_select_color);
        //不同Menu打开不同的Fragment

        int i = 0;
        TabHost.TabSpec tabSpec_0 = fragment_tab_host.newTabSpec(i + "");
        tabSpec_0.setIndicator(getMenuView(i));
        fragment_tab_host.addTab(tabSpec_0, ShowPageFragment.class, null);

        i++;
        TabHost.TabSpec tabSpec_1 = fragment_tab_host.newTabSpec(i + "");
        tabSpec_1.setIndicator(getMenuView(i));
        fragment_tab_host.addTab(tabSpec_1, AddFragment.class, null);

        i++;
        TabHost.TabSpec tabSpec_2 = fragment_tab_host.newTabSpec(i + "");
        tabSpec_2.setIndicator(getMenuView(i));
        fragment_tab_host.addTab(tabSpec_2, ManageAccountFragment.class, null);
    }

    private View getMenuView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_item_main_menu, null, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_menu);
        textView.setTextColor(Utils.getColorStateList(menu_nol_color, menu_on_color, menu_on_color, menu_nol_color));
        textView.setText(menu_str_arr[position]);
        ImageView iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        iv_menu.setImageDrawable(Utils.getDrawableSelector(getResources().getDrawable(menu_on_ids[position]), getResources().getDrawable(menu_nol_ids[position])));
        return view;
    }

    @Override
    public boolean onTabClick(int position) {
        return false;
    }

    @Override
    public void onTabSelected(int position) {
        lastPosition = position;
        nowPosition = position;
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchTab();
    }

    public void explicitSwitchTab(int pos)
    {
        nowPosition = pos;
        switchTab();
    }


    public void switchTab() {
        if (lastPosition != nowPosition) {
            lastPosition = nowPosition;
            tabWidget.focusCurrentTab(nowPosition);
            fragment_tab_host.setCurrentTab(nowPosition);
        }
    }
        public  void logout(View view){
            SharedPreferences sharedpreferences = getSharedPreferences("loginsp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
        }

    }

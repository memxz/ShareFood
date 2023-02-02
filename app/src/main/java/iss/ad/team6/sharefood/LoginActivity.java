package iss.ad.team6.sharefood;


import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import com.google.gson.Gson;

import java.io.IOException;

import iss.ad.team6.sharefood.Gson.Constant;
import iss.ad.team6.sharefood.Gson.Gson_User;
import iss.ad.team6.sharefood.Gson.Persons;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
//edge://flags/

/**
 * Login
 */
public class LoginActivity extends AppCompatActivity
{
    private EditText et_Account;//账号输入框
    private EditText et_Password;//密码输入框

    //private Button LoginButton;
    private String username;
    private String password;
    private Gson_User user;
    public String body=null;
    private ImageView ivPwdSwitch;
    private Boolean bPwdSwitch=false;
    private CheckBox rememberPass;
    private CheckBox agress;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LinearLayout Log_lin;
    private int from = 0;
    private PopupWindow popupWindow;
    private TextView TK;
    private TextView YS;
    private TextView TK2;
    private TextView YS2;
    private Button btn_disagress;
    private Button btn_agress;
    private ImageView img_del;
    private ImageView img_del2;
    private TextView registerAccount;
    private ImageView loginButtonn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /**
         * Hide status
         */
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_Account=findViewById(R.id.et_account);//账号输入框
        et_Password=findViewById(R.id.et_pwd);//密码输入框

        ivPwdSwitch=findViewById(R.id.iv_pwd_switch);//密码是否可见

        img_del = findViewById(R.id.iv_et_num_delete);//清空账号栏的EditText
        img_del2 = findViewById(R.id.iv_et_pwd_delete);//清空密码栏的EditText

        agress = findViewById(R.id.ck_xieyi);//同意服务条款checkbox
        Log_lin = findViewById(R.id.log_lin);////同意服务条款的线性布局
        TK = findViewById(R.id.fuwutiaokuan);//服务条款点击
        YS = findViewById(R.id.YSZC);//隐私政策

        registerAccount = findViewById(R.id.registered_account);//注册按钮
        /**
         * 注册按钮监听事件
         */
        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_Account.getText().toString();//获取账号输入框的账号
                password = et_Password.getText().toString();//获取密码输入框的密码
                //跳转活动
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 清空账号栏的EditText监听事件
         */
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_Account.setText("");
            }
        });

        /**
         * 清空密码栏的EditText监听事件
         */
        img_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_Password.setText("");
            }
        });

        /**
         * 服务条款的监听事件
         */
        TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,FWTK.class);
                startActivity(intent);
            }
        });

        /**
         * 隐私政策的监听事件
         */
//        YS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this,YSZC.class);
//                startActivity(intent);
//            }
//        });

        rememberPass = findViewById(R.id.remember_pass);//记住密码checkbox

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);//记住密码，如果为空就为false
        //如果勾选记住密码，将账号和密码都设置到文本框中
        if (isRemember) {
            String account1 = pref.getString("account", "");//第一次为空
            String password1 = pref.getString("password", "");//第一次为空
            et_Account.setText(account1);
            et_Password.setText(password1);
            rememberPass.setChecked(true);//记住密码打勾
        }

        loginButtonn = findViewById(R.id.btn_login);//登录按钮
        /**
         * 登录的监听事件
         */
        loginButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                username=et_Account.getText().toString();//输入框  账号
                password=et_Password.getText().toString();//输入框  密码
                editor=pref.edit();

                if (rememberPass.isChecked()) { // 检查复选框是否被选中
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", username);
                    editor.putString("password", password);
                    editor.putBoolean("autologin", true);
                } else {
                    editor.clear();//清除账号密码框的数据
                }
                //输入检测：如果有一个为空，则提示不能登录
                if(username.equals("")||password.equals(""))
                {
                    Toast.makeText(LoginActivity.this,"Account or Password cannot be blank",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    editor.apply();
                    if (agress.isChecked()){//点击了同意协议
                        post();//登录
                    }else {//否则  弹出提示的框
                        from = Location2.BOTTOM.ordinal();//从底部弹出窗口
                        initPopupWindow2();
                    }

                }

            }
        });

        /**
         * 密码是否可见的监听事件
         */
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPwdSwitch = !bPwdSwitch;
                if(bPwdSwitch)
                {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                    et_Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    //输入一个密码或者 输入类型为普通文本
                    et_Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    //设置字体样式
                    et_Password.setTypeface(Typeface.DEFAULT);
                }
            }
        });
    }



    private void post()
    {
        new Thread(() -> {

            String urlParam = "?" + "&password=" + password + "&username=" + username;
            Log.d("LoginActivity_urlParam",urlParam);
            Headers headers = new Headers.Builder()
                    .add("appId", Constant.APPID)
                    .add("appSecret", Constant.APPSECRET)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .url("https://8094de54-7fbc-4762-bfe8-9a8dfbd29834.mock.pstmn.io/login"+ urlParam)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();

            try
            {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(logincallback);
            }
            catch (NetworkOnMainThreadException ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    private final Callback logincallback=new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException
        {
            ResponseBody responseBody = response.body();
            body = responseBody.string();
            Log.d("LoginActivity_Login", "Response_body : " + body);

            if (response.isSuccessful()) {
                runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Persons responseParse = gson.fromJson(body, Persons.class);
                    user = responseParse.getUser();
                    Message message = new Message();
                    switch (responseParse.getMsg()) {
                        case "Login Successfully":
                            message.arg1 = 1;
                            break;
                        case "Wrong Password":
                            message.arg1 = 2;
                            break;
                        case "Current User is not existed":
                            message.arg1 = 3;
                            break;
                        case "Input Can Not Be Blank":
                            message.arg1 = 4;
                            break;
                        default:
                            message.arg1 = -1;
                            break;
                    }
                    mLoginHandler.sendMessage(message);
                });
            }
        }
    };



    private final Handler mLoginHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            //登录成功
            if (msg.arg1 == 1) {
                Intent intent=new Intent();
                intent.putExtra("userInfo",body);
                //想要实现通过intent开启一个activity，并将这个activity放至栈底或者清空栈后再把这个activity压进栈去。
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //不需要返回值的跳转
                intent.setClass(LoginActivity.this,Menu_Activity.class);
                startActivity(intent);
            }
            if (msg.arg1 == 2)
                Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
            if (msg.arg1 == 3)
                Toast.makeText(LoginActivity.this, "user is Not Existed!", Toast.LENGTH_SHORT).show();
            if (msg.arg1 == 4)
                Toast.makeText(LoginActivity.this, "Account or Password cannot be blank!", Toast.LENGTH_SHORT).show();
            if (msg.arg1 == -1)
                Toast.makeText(LoginActivity.this, "Network Connection Error!", Toast.LENGTH_SHORT).show();
        }
    };

    protected void initPopupWindow2() {
        //用getLayoutinflate.inflate方法得到我们要显示的xml文件
        View popupWindowView = getLayoutInflater().inflate(R.layout.agreement, null);

        /**
         *
         public PopupWindow(View contentView, int width, int height, boolean focusable)
         */
        if (Location2.BOTTOM.ordinal() == from) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }

        //设置弹出动画
        if (Location2.BOTTOM.ordinal() == from) {
            popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        }
//        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
//        popupWindow.setBackgroundDrawable(dw);

        //控制弹窗的位置
        if (Location2.BOTTOM.ordinal() == from) {
            popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.agreement, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 100, 0);
        }

        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new popupDismissListener());

        /**
         *  return false 我监听器监听到了，剩下的系统你自己去处理我不管了
         *
         *  return true 我监听器监听到了，今儿这档子事我管了我自个儿处理，系统你一边先凉快去
         */
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        //服务条款
        TK2 = popupWindowView.findViewById(R.id.futk2);
        //隐私政策
        YS2 = popupWindowView.findViewById(R.id.yszc2);

        btn_disagress = popupWindowView.findViewById(R.id.btn_disagr);//按钮  不同意
        btn_agress = popupWindowView.findViewById(R.id.btn_agre);// 按钮   同意

        /**
         * 服务条款监听事件
         */
        TK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,FWTK.class);
                startActivity(intent);
            }
        });

        /**
         * 隐私政策的监听事件
         */
//        YS2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this,YSZC.class);
//                startActivity(intent);
//            }
//        });

        /**
         * 按钮不同意的监听事件
         */
        btn_disagress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    public void run() {
                        try {
                            /**
                             * 相当于点击一次back键使得popwindow消失
                             */
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        } catch (Exception e) {

                        }
                    }
                }.start();

            }
        });

        /**
         * 按钮同意的监听事件
         */
        btn_agress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agress.setChecked(true);
                post();
            }
        });

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            // 想干啥自己写在这里
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    /**
     * 当popwindow消失时候，把屏幕透明度恢复
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    //设置整个屏幕的透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    public enum Location2 {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;
    }



}
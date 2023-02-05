package iss.ad.team6.sharefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
        Button bu=null;
        Button bu2=null;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            bu=findViewById(R.id.button2);
            bu2=findViewById(R.id.button3);
        }

        public  void logout(View view){
            SharedPreferences sharedpreferences = getSharedPreferences("mypsd", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
        }

        public void close(View view){
            finish();
        }
    }

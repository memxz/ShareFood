package iss.ad.team6.sharefood.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import iss.ad.team6.sharefood.EditAccountActivity;
import iss.ad.team6.sharefood.LoginActivity;
import iss.ad.team6.sharefood.R;

import iss.ad.team6.sharefood.RegisterActivity;

public class ManageAccountFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_account, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view){
        LinearLayout menu_0 = view.findViewById(R.id.menu_0);
        LinearLayout menu_1 = view.findViewById(R.id.menu_1);
        LinearLayout menu_2 = view.findViewById(R.id.menu_2);
        menu_0.setOnClickListener(this);
        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        switch (vid){
            case R.id.menu_0:
                {
                    Intent intent = new Intent(getActivity(), EditAccountActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent,200);
                }
                break;
            case R.id.menu_1:
                //TODO 待订单做完后,加上管理历史账单
                break;
            case R.id.menu_2://todo logout
            {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case 200:
                //user account has edit  succ, finish mainactivity
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

    }
}

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

import iss.ad.team6.sharefood.GEditFoodActivity;
import iss.ad.team6.sharefood.HistoryActivity;
import iss.ad.team6.sharefood.LoginActivity;
import iss.ad.team6.sharefood.R;
import iss.ad.team6.sharefood.EditAccountActivity;

public class GManageAccountFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.g_fragment_manage_account, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        LinearLayout menu_0 = view.findViewById(R.id.menu_0);
        LinearLayout menu_1 = view.findViewById(R.id.menu_1);
        LinearLayout menu_2 = view.findViewById(R.id.menu_2);
        LinearLayout menu_3 = view.findViewById(R.id.menu_3);
        menu_0.setOnClickListener(this);
        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
        menu_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        switch (vid) {
            case R.id.menu_0: {
                Intent intent = new Intent(getActivity(), EditAccountActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 200);
            }
            break;
            case R.id.menu_1: {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.menu_2://todo logout
            {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;
            case R.id.menu_3:
            {
                Intent intent = new Intent(getActivity(), GEditFoodActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                //user account has edit  succ, finish mainactivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

    }
}

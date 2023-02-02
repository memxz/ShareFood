package iss.ad.team6.sharefood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;

public class PhotoDiscoveryFragment extends Fragment {
    private Toolbar toolbar;
    private ListView lvPhotoList;
    private String userId;
    private String username;
    private String userHeadimg;
    private SwipeRefreshLayout sr_refresh;
    private File currentFile;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photodiscovery, container, false);


        return view;
    }



}
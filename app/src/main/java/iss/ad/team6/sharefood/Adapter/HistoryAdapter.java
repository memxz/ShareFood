package iss.ad.team6.sharefood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import iss.ad.team6.sharefood.R;
import iss.ad.team6.sharefood.bean.FoodBean;

public class HistoryAdapter extends BaseAdapter {
    private List<FoodBean> datas;

    public void setDatas(List<FoodBean> datas) {
        this.datas = datas;
    }

    public List<FoodBean> getDatas() {
        return datas;
    }

    @Override
    public int getCount() {
        return datas ==null?0:datas.size();
    }

    @Override
    public FoodBean getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getFoodId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view==null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history,parent,false);
        }

        ImageView imageView= view.findViewById(R.id.imageview);
        TextView title=view.findViewById(R.id.foodName);
        TextView description=view.findViewById(R.id.foodDescription);

        FoodBean data = getItem(i);

        String imageUrl = data.getImgUrl();
        Picasso.with(parent.getContext()).load(imageUrl).resize(100, 100).into(imageView);
        title.setText(data.getTitle());
        description.setText(data.getDescription());

        return view;
    }
}

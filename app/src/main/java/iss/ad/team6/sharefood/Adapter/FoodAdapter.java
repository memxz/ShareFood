package iss.ad.team6.sharefood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import iss.ad.team6.sharefood.R;
import iss.ad.team6.sharefood.bean.FoodBean;
import iss.ad.team6.sharefood.fragment.ShowPageFragment;

public class FoodAdapter extends ArrayAdapter<FoodBean> {
    protected List<FoodBean> foodList;
    private final ShowPageFragment context;


    public FoodAdapter(ShowPageFragment context, List<FoodBean> foodList) {
        super(context.getActivity(), 0,foodList);
        this.context = context;
        this.foodList=foodList;
    }
    public View getView(int position, View view, @NotNull ViewGroup parent)
    {
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.show_food_list,parent,false);
        }
        ImageView imageView= view.findViewById(R.id.imageview);
        //int id=context.getResources().getIdentifier(foodList.get(position).getImg(),"drawable", context.getPackageName());//暂时用该方法，图片处理还需要考虑
        //imageView.setImageResource(id);
        String imageUrl = foodList.get(position).getImgUrl();
        Picasso.with(context.getContext()).load(imageUrl).resize(100, 100).into(imageView);

        TextView title=view.findViewById(R.id.foodName);
        title.setText(foodList.get(position).getTitle());

        TextView description=view.findViewById(R.id.foodDescription);
        description.setText(foodList.get(position).getDescription());

        return view;
    }



}

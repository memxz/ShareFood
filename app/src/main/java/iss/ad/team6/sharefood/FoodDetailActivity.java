package iss.ad.team6.sharefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import iss.ad.team6.sharefood.Model.Food;

public class FoodDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.food_details);
        Intent intent = getIntent();
        Food foodInfo = (Food)intent.getSerializableExtra(ShowPageActivity.SELECTED_FOOD);
        if(foodInfo == null) {
            Log.e("Food", "No food info!");
            return;
        }

        ImageView foodImageView = findViewById(R.id.foodItemImg);
        if(foodImageView != null)
        {
            String imageUrl = foodInfo.getImg();
            Picasso.with(this).load(imageUrl).into(foodImageView);
        }

        TextView foodNameTextView = findViewById(R.id.FoodDetailsName);
        if(foodNameTextView != null)
        {
            foodNameTextView.setText(foodInfo.getTitle());
        }

        TextView foodDetailTextView = findViewById(R.id.FoodDetailsDescription);
        if(foodDetailTextView != null)
        {
            foodDetailTextView.setText(foodInfo.getDescription());
        }
    }
}
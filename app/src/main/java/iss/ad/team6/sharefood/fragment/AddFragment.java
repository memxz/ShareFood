package iss.ad.team6.sharefood.fragment;

import static android.content.Context.MODE_PRIVATE;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.widget.Toast;

import iss.ad.team6.sharefood.MainActivity;
import iss.ad.team6.sharefood.R;
import iss.ad.team6.sharefood.bean.FoodBean;
import iss.ad.team6.sharefood.bean.FoodType;
import iss.ad.team6.sharefood.bean.LoginBean;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFragment<OkHttpClient, FormBody> extends Fragment {

    FoodBean newFood;
    EditText foodTitle;
    EditText foodDescription;
    EditText foodLocation;
    EditText foodListDays;
    RadioGroup addRadioGroup;
    RadioButton addRb;
    Button selectImg;
    Button createFood;
    Uri imageUri;
    ImageView foodImage;
    String encImg;
    public final String createFoodUrl="https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/save";
    public final String saveFoodImgUrl = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/image/upload";

    public AddFragment() {
    }

    public static final int REQUEST_CODE_IMAGE = 100;

    ActivityResultLauncher mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        System.out.println(intent.toString());
                        imageUri = intent.getData();
                        foodImage.setImageURI(imageUri);
                    }
                }
            });

    private void BackToPrevious()
    {
        Toast.makeText(getContext(),"Food Added",Toast.LENGTH_LONG).show();
        ((MainActivity)getActivity()).explicitSwitchTab(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_addfood, container, false);

        foodImage = view.findViewById(R.id.upload_image);
        imageUri = null;
        selectImg = view.findViewById(R.id.btn_select_image);
        addRadioGroup=view.findViewById(R.id.add_food_type);
        if(selectImg!=null) {
            selectImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(getActivity())
                            .crop(1f, 1f)                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .createIntent(new Function1<Intent, Unit>() {
                                @Override
                                public Unit invoke(Intent Intent) {
                                    mStartForResult.launch(Intent);
                                    return null;
                                }
                            });
                }
            });
        }
        createFood=view.findViewById(R.id.btn_create_food);
        if(createFood!=null)
        {
            createFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoodBean newFood = new FoodBean();
                    foodTitle=view.findViewById(R.id.add_food_title);
                    String title=foodTitle.getText().toString();
                    if(title.equals(""))
                    {
                        Toast.makeText(getContext(),"Title cannot be empty",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newFood.setTitle(title);

                    foodDescription=view.findViewById(R.id.add_food_description);
                    String foodDes=foodDescription.getText().toString();
                    if(foodDes.equals(""))
                    {
                        Toast.makeText(getContext(),"Description cannot be empty",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newFood.setDescription(foodDes);

                    foodListDays=view.findViewById(R.id.add_list_days);
                    String days=foodListDays.getText().toString();
                    Double listDays = -1.0;
                    try {
                        listDays = Double.parseDouble(days);
                    }
                    catch(NumberFormatException e) {
                        listDays = -1.0;
                    }
                    if(listDays <= 0.01)
                    {
                        Toast.makeText(getContext(),"Invalid list days",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newFood.setListDays(listDays);

                    SharedPreferences pref = getActivity().getSharedPreferences("loginsp",MODE_PRIVATE);
                    String userJson = pref.getString("userBeanJson","");
                    LoginBean user=new Gson().fromJson(userJson,LoginBean.class);
                    newFood.setPublisher(user);

                    //EditText foodLocation;  wait for cyrus import google map sdk
                    String halaStatus="";
                    //addRadioGroup=view.findViewById(R.id.add_food_type);
                    for(int i=0;i<addRadioGroup.getChildCount();i++)
                    {
                        /*if(addRadioGroup.getChildAt(i).getClass() != RadioButton.class)
                            continue;*/
                        RadioButton addRb=(RadioButton) addRadioGroup.getChildAt(i);
                        if(addRb.isChecked())
                        {
                            halaStatus=addRb.getText().toString();
                            switch (halaStatus)
                            {
                                case "Halal":
                                     {newFood.setFoodType(FoodType.halal); break;}
                                case "Non-Halal":
                                    {newFood.setFoodType(FoodType.nonhalal);}
                            }
                        }
                    }

                    if(imageUri == null)
                    {
                        Toast.makeText(getContext(),"No image!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    encImg = "";
                    final InputStream imageStream;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);
                        byte[] b = baos.toByteArray();
                        encImg = Base64.encodeToString(b, Base64.DEFAULT);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    if(encImg.equals(""))
                    {
                        Toast.makeText(getContext(),"Image encode failed!",Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Gson post=new Gson();
                    Map<String,Object> createFoodMap=new HashMap<String,Object>();

                    String JsonStr=post.toJson(newFood);
                    String method = "POST";
                    SaveFoodHttpHandler createHandler= new SaveFoodHttpHandler();
                    createHandler.execute(createFoodUrl, method, JsonStr);
                }
            });
        }
        return view;
    }

    public class SaveFoodImgHttpHandler extends AsyncTask {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

        @Override
        protected Object doInBackground(Object[] params) {
            String jsonStr = (String)params[0];
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonStr);
            Request request = new Request.Builder()
                    .url(saveFoodImgUrl)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            BackToPrevious();


        }
    }

    public class SaveFoodHttpHandler extends AsyncTask {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();


        @Override
        protected Object doInBackground(Object[] params) {


            Request.Builder builder = new Request.Builder();
            builder.url((String) params[0]);
            String method = (String) params[1];
            if(method == "GET")
            {
                // Do nothing
            }
            else if(method == "POST")
            {
                String json = (String)params[2];
                builder.post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), json));
            }

            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            FoodBean food;
            String jsonStr = (String)o;
            Gson gson=new GsonBuilder().disableHtmlEscaping().create();
            food = gson.fromJson(jsonStr, FoodBean.class);

            encImg = encImg.replace("\n", "");
            Map<String,String> inputMap=new HashMap<String,String>();
            inputMap.put("id", food.getFoodId().toString());
            inputMap.put("base64", encImg);
            jsonStr=gson.toJson(inputMap);

            SaveFoodImgHttpHandler saveFoodImgHttpHandler = new SaveFoodImgHttpHandler();
            saveFoodImgHttpHandler.execute(jsonStr);

            Log.d("successful", "successful");
        }

    }

}


package iss.ad.team6.sharefood.fragment;

import static android.content.Context.MODE_PRIVATE;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.Manifest;
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
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class AddFragment<OkHttpClient, FormBody> extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;

    private SharedPreferences pref;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mGetedLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // [END maps_current_place_state_keys]

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    double lat;
    double lng;
    //end maprelated
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
    EditText et_foodLocation;
    public final String createFoodUrl = "https://card-service-cloudrun-lmgpq3qg3a-et.a.run.app/card-service/api/food/save";
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

    private void BackToPrevious() {
        Toast.makeText(getContext(), "Food Added", Toast.LENGTH_LONG).show();
        ((MainActivity) getActivity()).explicitSwitchTab(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getContext(), MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
            @Override
            public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
                //println(it.name)
                Log.d("TAG", "onMapsSdkInitialized: ");
            }
        });
        //newly add,this line to make the options appear in Toolbar
        setHasOptionsMenu(true);
        // [START_EXCLUDE silent]
        // [START maps_current_place_on_create_save_instance_state]
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // [END maps_current_place_on_create_save_instance_state]
        // [END_EXCLUDE]
        View view = inflater.inflate(R.layout.activity_addfood, container, false);
        // [START_EXCLUDE silent]
        // Construct a PlacesClient
        Places.initialize(getContext(), getString(R.string.MAPS_API_KEY));
        placesClient = Places.createClient(getContext());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Build the map.
        // [START maps_current_place_map_fragment]
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        // [END maps_current_place_map_fragment]
        // [END_EXCLUDE]

        getLocationWithCheckNetworkAndGPS();

        foodImage = view.findViewById(R.id.upload_image);
        imageUri = null;
        selectImg = view.findViewById(R.id.btn_select_image);
        addRadioGroup = view.findViewById(R.id.add_food_type);
        et_foodLocation=view.findViewById(R.id.add_food_location);
        if (selectImg != null) {
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
        createFood = view.findViewById(R.id.btn_create_food);
        if (createFood != null) {
            createFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoodBean newFood = new FoodBean();
                    foodTitle = view.findViewById(R.id.add_food_title);
                    String title = foodTitle.getText().toString();
                    if (title.equals("")) {
                        Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newFood.setTitle(title);

                    foodDescription = view.findViewById(R.id.add_food_description);
                    String foodDes = foodDescription.getText().toString();
                    if (foodDes.equals("")) {
                        Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newFood.setDescription(foodDes);

                    foodListDays = view.findViewById(R.id.add_list_days);
                    String days = foodListDays.getText().toString();
                    Double listDays = -1.0;
                    try {
                        listDays = Double.parseDouble(days);
                    } catch (NumberFormatException e) {
                        listDays = -1.0;
                    }
                    if (listDays <= 0.01) {
                        Toast.makeText(getContext(), "Invalid list days", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    newFood.setListDays(listDays);

                    SharedPreferences pref = getActivity().getSharedPreferences("loginsp", MODE_PRIVATE);
                    String userJson = pref.getString("userBeanJson", "");
                    LoginBean user = new Gson().fromJson(userJson, LoginBean.class);
                    newFood.setPublisher(user);

                    //EditText foodLocation;  wait for cyrus import google map sdk
                    String fdLocation=et_foodLocation.getText().toString();
                    if(fdLocation.equals(""))
                    {
                        Toast.makeText(getContext(),"PostCode cannot be empty and must be 6 digit number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(lat==0 || lng==0){
                        Toast.makeText(getContext(),"Cannnot get your current location , pls click to navigate",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("111111 Lag_long",lat+"-"+lng);
                    newFood.setLatitude(lat);
                    newFood.setLongitude(lng);
                    Log.d("111111 Lag_long2",newFood.getLatitude()+"-"+newFood.getLongitude());
                    newFood.setFoodLocation(fdLocation);
                    newFood.setListed(true);
                    String halaStatus = "";
                    //addRadioGroup=view.findViewById(R.id.add_food_type);
                    for (int i = 0; i < addRadioGroup.getChildCount(); i++) {
                        /*if(addRadioGroup.getChildAt(i).getClass() != RadioButton.class)
                            continue;*/
                        RadioButton addRb = (RadioButton) addRadioGroup.getChildAt(i);
                        if (addRb.isChecked()) {
                            halaStatus = addRb.getText().toString();
                            switch (halaStatus) {
                                case "Halal": {
                                    newFood.setFoodType(FoodType.halal);
                                    break;
                                }
                                case "Non-Halal": {
                                    newFood.setFoodType(FoodType.nonhalal);
                                }
                            }
                        }
                    }

                    if (imageUri == null) {
                        Toast.makeText(getContext(), "No image!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    encImg = "";
                    final InputStream imageStream;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] b = baos.toByteArray();
                        encImg = Base64.encodeToString(b, Base64.DEFAULT);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (encImg.equals("")) {
                        Toast.makeText(getContext(), "Image encode failed!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Gson post = new Gson();
                    Map<String, Object> createFoodMap = new HashMap<String, Object>();

                    String JsonStr = post.toJson(newFood);
                    String method = "POST";
                    Log.d("111111 Lag_long3",JsonStr);
                    SaveFoodHttpHandler createHandler = new SaveFoodHttpHandler();
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
            String jsonStr = (String) params[0];
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
            } catch (Exception e) {
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
            if (method == "GET") {
                // Do nothing
            } else if (method == "POST") {
                String json = (String) params[2];
                builder.post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), json));
            }

            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            FoodBean food;
            String jsonStr = (String) o;
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            food = gson.fromJson(jsonStr, FoodBean.class);

            encImg = encImg.replace("\n", "");
            Map<String, String> inputMap = new HashMap<String, String>();
            inputMap.put("id", food.getFoodId().toString());
            inputMap.put("base64", encImg);
            jsonStr = gson.toJson(inputMap);

            SaveFoodImgHttpHandler saveFoodImgHttpHandler = new SaveFoodImgHttpHandler();
            saveFoodImgHttpHandler.execute(jsonStr);

            Log.d("successful", "successful");
        }

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }
    // [END maps_current_place_on_save_instance_state]

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.current_place_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    // [START maps_current_place_on_options_item_selected]
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }
    // [END maps_current_place_on_options_item_selected]

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    // [START maps_current_place_on_map_ready]
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // [START_EXCLUDE]
        // [START map_current_place_set_info_window_adapter]
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = View.inflate(getContext(), R.layout.custom_info_contents,
                        (LinearLayout) getView().findViewById(R.id.map2));

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission();
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }
    // [END maps_current_place_on_map_ready]

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }

                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    // [END maps_current_place_get_device_location]

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    // [END maps_current_place_location_permission]

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }
    // [END maps_current_place_on_request_permissions_result]

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        AddFragment.this.openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }
    // [END maps_current_place_show_current_place]

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    // [START maps_current_place_open_places_dialog]
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }
    // [END maps_current_place_open_places_dialog]

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    // [END maps_current_place_update_location_ui]

    public void getLocationWithCheckNetworkAndGPS() {
        Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;


        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(bestProvider);

        if (location == null){
            Toast.makeText(getContext(),"Location Not found",Toast.LENGTH_LONG).show();
        }else{
            geocoder = new Geocoder(getContext());
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                lat=(double)user.get(0).getLatitude();
                lng=(double)user.get(0).getLongitude();

                pref = this.getActivity().getSharedPreferences("loginsp", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putFloat("Latitude", (float)lat);
                editor.putFloat("Longitude", (float)lng);
                editor.commit();
                System.out.println(" DDD lat: " +lat+",  longitude: "+lng);
                Log.d("11111 Location: ", "Latitude"+lat+"longitude: "+lng);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


package chauhuynh.info.demonearbyplaces.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import chauhuynh.info.demonearbyplaces.R;
import chauhuynh.info.demonearbyplaces.adapter.AdapterSuggestSearch;
import chauhuynh.info.demonearbyplaces.api.CallBackApi;
import chauhuynh.info.demonearbyplaces.api.CallBackListApi;
import chauhuynh.info.demonearbyplaces.api.HandleApi;
import chauhuynh.info.demonearbyplaces.dialog.DialogPlaceDetail;
import chauhuynh.info.demonearbyplaces.log.Logcat;
import chauhuynh.info.demonearbyplaces.model.Geometry;
import chauhuynh.info.demonearbyplaces.model.Predictions;
import chauhuynh.info.demonearbyplaces.model.Result;
import chauhuynh.info.demonearbyplaces.model.Results;
import chauhuynh.info.demonearbyplaces.utils.Keyboard;
import chauhuynh.info.demonearbyplaces.utils.Utils;

/**
 * Created by appro on 05/03/2018.
 */

public class MyLocation extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private static final long MIN_TIME_FOR_UPDATE = 1000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Marker marker;

    private EditText edt_input_search;
    private RelativeLayout layoutSuggestSearch;
    private ListView listViewSuggestSearch;
    private AdapterSuggestSearch adapterSuggestSearch;
    private List<Predictions> listSuggestSearch;

    private List<Results> listResults = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private double latitude, longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        setupBottomNavigationView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.getInstance().isWifi(this)) {
            Utils.getInstance().alert(this, "Wifi", getString(R.string.app_name), "Turn on the Wifi", "OK", "");
        } else {
            initMap();
            initInputSuggestSearch();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Toast request permission
                Toast.makeText(this, "Permission to use Camera", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            //Goto Continue
            initializeLocationAPI();
            onMarkerClick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Goto Continue
                initializeLocationAPI();
                onMarkerClick();


            } else {
                //Deny Permission
                Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressWarnings("unchecked")
    private void initInputSuggestSearch() {
        edt_input_search = findViewById(R.id.edt_input);
        layoutSuggestSearch = findViewById(R.id.layout_suggest_search);
        listViewSuggestSearch = findViewById(R.id.list_suggest_search);
        listSuggestSearch = new ArrayList<>();
        adapterSuggestSearch = new AdapterSuggestSearch(this, listSuggestSearch);
        listViewSuggestSearch.setAdapter(adapterSuggestSearch);

        listViewSuggestSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edt_input_search.setText(listSuggestSearch.get(position).getDescription());
                layoutSuggestSearch.setVisibility(View.GONE);
                Keyboard.hide(MyLocation.this);

                getDetailChooseLocation(listSuggestSearch.get(position).getPlace_id());

            }
        });

        edt_input_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

                slide_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        edt_input_search.setText(null);
                        layoutSuggestSearch.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                layoutSuggestSearch.startAnimation(slide_down);
            }
        });

        edt_input_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layoutSuggestSearch.setVisibility(View.GONE);
            }
        });

        edt_input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (layoutSuggestSearch.getVisibility() == View.GONE) {
                    return;
                }

                HandleApi.getInstance().getSuggestSearch(MyLocation.this, s.toString(), new CallBackListApi() {
                    @Override
                    public void resultApi(List<Object> listObject) {
                        listSuggestSearch = (List<Predictions>) (Object) listObject;
                        adapterSuggestSearch = new AdapterSuggestSearch(MyLocation.this, listSuggestSearch);
                        listViewSuggestSearch.setAdapter(adapterSuggestSearch);
                    }
                });
            }
        });
    }

    private void getDetailChooseLocation(String place_id) {
        HandleApi.getInstance().getDetailPlaces(this, place_id, new CallBackApi() {
            @Override
            public void resultApi(Object object) {
                Result result = (Result) object;
                Geometry geometry = result.getGeometry();
                if (geometry != null) {
                    chauhuynh.info.demonearbyplaces.model.Location location = geometry.getLocation();

                    if (marker != null) {
                        marker.remove();
                    }

                    latitude = Double.parseDouble(location.getLat());
                    longitude = Double.parseDouble(location.getLng());

                    addMarkerToMap(latitude, longitude);

                    //getNearbyPlaces(latitude, longitude, "tea");
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getNearbyPlaces(double latitude, double longitude, String type) {
        HandleApi.getInstance().getNearbyPlaces(this, latitude, longitude, type, new CallBackListApi() {
            @Override
            public void resultApi(List<Object> listObject) {
                listResults = (List<Results>) (Object) listObject;
                for (int i = 0; i < listResults.size(); i++) {
                    Results results = listResults.get(i);
                    double lat = Double.parseDouble(results.getGeometry().getLocation().getLat());
                    double lng = Double.parseDouble(results.getGeometry().getLocation().getLng());

                    String placeName = results.getName();
                    String vicinity = results.getVicinity();

                    addNearbyMarkerToMap(lat, lng, placeName, i);
                }
            }
        });
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.botton_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cafeshop:
                        getNearbyPlaces(latitude, longitude, "coffee");
                        break;
                    case R.id.restaurant:
                        getNearbyPlaces(latitude, longitude, "restaurant");
                        break;
                }
                return true;
            }
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initializeLocationAPI() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {

            createLocationRequest();
            mGoogleApiClient.connect();

        }
    }

    private void createLocationRequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void stopLocationRequest() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void onConnectedMap() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_FOR_UPDATE);
        mLocationRequest.setFastestInterval(MIN_TIME_FOR_UPDATE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void onMarkerClick() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String snippet = marker.getSnippet();
                if (snippet != null) {
                    int i = Integer.parseInt(snippet);
                    DialogPlaceDetail.getInstance().show(MyLocation.this, listResults.get(i));
                }
                return false;
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        onConnectedMap();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.reconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Logcat.write("MyLocation:", location.toString());

        addMarkerToMap(location.getLatitude(), location.getLongitude());

        stopLocationRequest();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Utils.getInstance().isLocation(this)) {
            requestPermission();
        } else {
            Utils.getInstance().alert(this, "GPS", getString(R.string.app_name), "Turn on the Location", "OK", "");
        }
    }

    private void addMarkerToMap(double latitude, double longitude) {

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        marker = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }

    private void addNearbyMarkerToMap(double latitude, double longitude, String placeName, int position) {

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(placeName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions.snippet(String.valueOf(position));

        marker = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }

    @Override
    public void onBackPressed() {

        if (layoutSuggestSearch.getVisibility() == View.VISIBLE) {
            layoutSuggestSearch.setVisibility(View.GONE);
            return;
        }
        finish();

    }
}

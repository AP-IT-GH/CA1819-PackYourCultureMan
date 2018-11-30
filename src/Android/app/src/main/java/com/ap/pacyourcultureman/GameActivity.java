package com.ap.pacyourcultureman;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.BearingCalc;
import com.ap.pacyourcultureman.Helpers.CollisionDetection;
import com.ap.pacyourcultureman.Helpers.CollisionHandler;
import com.ap.pacyourcultureman.Helpers.JSONSerializer;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Ghost Blinky;
    private static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION = 1;
    List<Assignment> assignments = ApiHelper.assignments;
    List<Dot> dots = ApiHelper.dots;
    Location mLastLocation;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    Assignment currentAssigment;
    List<Marker> assigmentMarkers = new ArrayList<>();
    SlidingUpPanelLayout bottomPanel;
    TextView txtName, txtWebsite, txtShortDesc, txtLongDesc, txtCurrentScore, txtCurrentLifePoints, txtCurrentAssignment, txtCurrentHeading;
    ImageView imgSight;
    Marker selectedMarker;
    Location location;
    Circle Circle;
    List<Circle> circles;
    Marker perth;
    Player player = ApiHelper.player;
    int currentScore, userId;
    NavigationView navigationView;
    ApiHelper apiHelper;
    CollisionDetection collisionDetection;
    Intent iin;
    Bundle b;
    String jwt, bearingStr;
    static LatLng currentPos;
    static Boolean ghostCollide = false;
    static final LatLng PERTH = new LatLng(51.230663, 4.407146);

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        SharedPreferences sp = getSharedPreferences("DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("chbremember", false);
                        editor.putBoolean("chbloginauto", false);
                        editor.apply();
                        intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_stats:
                        intent = new Intent(getBaseContext(),StatsPage.class);
                        intent.putExtra("userid",userId);
                        Log.e("jwt", ApiHelper.player.getJwt());
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(getBaseContext(),Settings.class);
                        Log.e("jwt", ApiHelper.player.getJwt());
                        startActivity(intent);
                        break;
                    case R.id.nav_sights:
                        intent = new Intent(getBaseContext(),Sights.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        Blinky = new Ghost();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onPause(){
        super.onPause();

        // Stops Location Updates if activity is paused
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.227076, 4.417227), 15));
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500); // 0.5 second interval
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        currentAssigment = getRandomAssignment();
        //dots
        for (int i = 0; i < dots.size(); i++) {dots.get(i).Draw(mMap, getApplicationContext());}

        //Blinky draw and dummy movement
        Blinky.Draw(mMap, getApplicationContext());
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Blinky.FollowPath(new LatLng(1,1), new LatLng(1,1));
            }
        });
        Log.d("Movement", "Ik ben non-blocking");
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(51.217065, 4.397200));
        for(int i = 0; i < assignments.size(); i++) {
            Marker mark;
            LatLng assigmentMarker = new LatLng(assignments.get(i).lat, assignments.get(i).lon);
            mark = mMap.addMarker(new MarkerOptions().position(assigmentMarker).title(assignments.get(i).name));
            assigmentMarkers.add(mark);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION);
            }
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));

        if (!success) {
            Log.e("Style failed", "Style parsing failed.");
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("Draggable Marker loc: ", "latitude : "+ marker.getPosition().latitude + "longitude : " + marker.getPosition().longitude);
                currentPos = marker.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                if(collisionDetection.collisionDetect(marker.getPosition(), currentAssigment.latLng, 10)){
                    Log.d("assigmentHit", "assigmentHit");
                }
                for(int i = 0; i < dots.size(); i++) {
                    if(collisionDetection.collisionDetect(marker.getPosition(), new LatLng(dots.get(i).getLat(), dots.get(i).getLon()), 5)) {
                        Log.v("Dot", "dot hit");
                        currentScore++;
                        txtCurrentScore.setText("x " + currentScore);
                    }
                }
                BearingCalc bearingCalc = new BearingCalc();
              //  txtCurrentHeading.setText(bearingCalc.getBearingInString(bearingCalc.bearing(marker.getPosition().latitude, marker.getPosition().longitude, currentAssigment.lat, currentAssigment.lon)));
               // Log.d("Bearing", Double.toString(bearingCalc.bearing(marker.getPosition().latitude, marker.getPosition().longitude, currentAssigment.lat, currentAssigment.lon)));
                txtCurrentHeading.setText(bearingCalc.getBearingInString(marker.getPosition().latitude, marker.getPosition().longitude, currentAssigment.lat, currentAssigment.lon));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
        perth = mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .draggable(true));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // TODO: Disable Maps functionality
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                for(int i = 0; i < assignments.size(); i++) {
                    collisionDetection.collisionDetect(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(assignments.get(i).lat, assignments.get(i).lon), 10);

                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CircleOptions circleOptions = new CircleOptions();
                LatLng markable = perth.getPosition();
                collisionDetection.collisionDetect(markable, new LatLng(currentAssigment.lat, currentAssigment.lon), 10);
                Log.d(String.valueOf(markable.latitude), String.valueOf(markable.longitude));

                //dots collision
                for(int i = 0; i < dots.size(); i++) {
                    collisionDetection.collisionDetect(markable, new LatLng(dots.get(i).getLat(), dots.get(i).getLon()), 5);
                }
                if(ghostCollide) {
                    getRandomAssignment();
                    ghostCollide = false;
                    CollisionHandler collisionHandler = new CollisionHandler(GameActivity.this);
                    collisionHandler.ghostCollision();
                    if(player.getPlayerGameStats().getLifePoints() == 0) {
                                        getRandomAssignment();
                    }
                    txtCurrentLifePoints.setText("x " + player.getPlayerGameStats().getLifePoints());
                }
            }
        }
    };
    private Assignment getRandomAssignment() {
        if(Circle != null) {
            Circle.remove();
        }

        Random rand = new Random();
        int n = rand.nextInt(assignments.size());
        if(assignments.get(n) == currentAssigment) {
            n = rand.nextInt(assignments.size());
        }
        txtCurrentAssignment.setText(assignments.get(n).name);
        CircleOptions circleOptionsCurrentAssignment = new CircleOptions();
        circleOptionsCurrentAssignment.center(new LatLng(assignments.get(n).lat, assignments.get(n).lon));
                circleOptionsCurrentAssignment.radius(10);
                circleOptionsCurrentAssignment.strokeColor(Color.YELLOW);
                circleOptionsCurrentAssignment.fillColor(0x30ff0000);
                circleOptionsCurrentAssignment.strokeWidth(2);
                Circle = mMap.addCircle(circleOptionsCurrentAssignment);

        for(int i = 0; i < assignments.size() - 1; i++) {
            if (circles != null) {
                circles.get(i).remove();
            }
        }
        circles = new ArrayList<>();
        for(int i = 0; i < assignments.size(); i++) {
            if(i != n) {
                CircleOptions circleOptionsSafeZone = new CircleOptions();
                circleOptionsSafeZone.center(new LatLng(assignments.get(i).lat, assignments.get(i).lon));
                circleOptionsSafeZone.radius(10);
                circleOptionsSafeZone.strokeColor(Color.GREEN);
                circleOptionsSafeZone.fillColor(0x30ff0000);
                circleOptionsSafeZone.strokeWidth(2);
                Circle circle = mMap.addCircle(circleOptionsSafeZone);
                circles.add(circle);
            }
        }
        return assignments.get(n);
    }


    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for(int i = 0; i < assignments.size(); i++) {
            if (marker.equals(assigmentMarkers.get(i)))
            {
                bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                bottomPanel.setPanelHeight(400);
                txtName.setText(assignments.get(i).name);
                if(assignments.get(i).website != "N/A") {
                    txtWebsite.setVisibility(View.VISIBLE);
                    txtWebsite.setText(assignments.get(i).website);
                }
                else txtWebsite.setVisibility(View.INVISIBLE);
                Picasso.get().load(assignments.get(i).imgUrl).into(imgSight);
                txtShortDesc.setText(assignments.get(i).shortDescr);
                txtLongDesc.setText(assignments.get(i).longDescr);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("Pushed", "pushed");
       /*
        dots klikken moet uitstaan

       for(int i = 0; i < dots.size(); i++) {
            if (dots.get(i).equals(marker)){
                return false;
            }
        }*/
        selectedMarker = marker;
        return false;
    }
    @Override
    public void onBackPressed() {
        if (bottomPanel != null &&
                (bottomPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)) {
            bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        }
        else if(bottomPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        else if(selectedMarker != null) {
                selectedMarker.hideInfoWindow();
        } else super.onBackPressed();

    }
    private void initializer() {
        currentScore = 0;
        apiHelper = new ApiHelper();
        navigationView = (NavigationView) findViewById(R.id.menu);
        bottomPanel = findViewById(R.id.sliding_layout);
        bottomPanel.setPanelHeight(0);
        txtName = findViewById(R.id.txtName);
        imgSight = findViewById(R.id.imgSight);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtShortDesc = findViewById(R.id.txtShortDesc);
        txtLongDesc = findViewById(R.id.txtLongDesc);
        txtCurrentScore = findViewById(R.id.game_txt_currentscore);
        txtCurrentScore.setText("x " + currentScore);
        txtCurrentLifePoints = findViewById(R.id.game_txt_currentLifePoints);
        txtCurrentLifePoints.setText("x " + ApiHelper.player.getPlayerGameStats().getLifePoints());
        txtCurrentAssignment = findViewById(R.id.game_txt_currentAssginment);
        txtCurrentHeading = findViewById(R.id.game_txt_currentHeading);
        collisionDetection = new CollisionDetection();
        iin = getIntent();
        b = iin.getExtras();
        currentPos = PERTH;
        if(b!=null){
            userId = (int) b.get("userid");
            jwt = (String) b.get("jwt");
        }
    }
}

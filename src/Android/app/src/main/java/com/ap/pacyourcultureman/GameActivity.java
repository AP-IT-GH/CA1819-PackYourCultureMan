package com.ap.pacyourcultureman;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.BearingCalc;
import com.ap.pacyourcultureman.Helpers.CollisionDetection;
import com.ap.pacyourcultureman.Helpers.CollisionHandler;
import com.ap.pacyourcultureman.Helpers.GunHandler;
import com.ap.pacyourcultureman.Menus.Gunmenu;
import com.ap.pacyourcultureman.Menus.NavigationMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, SensorEventListener{

    private Ghost Blinky;
    private static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION = 1;
    private List<Assignment> assignments;
    private List<Dot> correctedDots;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private SlidingUpPanelLayout bottomPanel;
    private TextView txtName, txtWebsite, txtShortDesc, txtLongDesc, txtCurrentScore, txtCurrentLifePoints, txtCurrentAssignment, txtCurrentHeading, txtCurrentDistance;
    private ImageView imgSight;
    private Marker selectedMarker;
    private Location location;
    private List<Circle> circles;
    private Player player;
    private int userId;
    private ApiHelper apiHelper;
    private CollisionDetection collisionDetection;
    private Intent iin;
    private Bundle b;
    private String jwt;
    private BearingCalc bearingCalc;
    private CollisionHandler collisionHandler;
    private Gunmenu gunmenu;
    private Handler handler;
    private GoogleMap mMap;
    private SensorManager mSensorManager;
    private Skins dragablePlayer, playerpos;
    public static LatLng currentPos;
    public static Boolean ghostCollide = false;
    public static LatLng pinnedLocation;
    public static Assignment currentAssigment;
    public static float rotation;
    public static Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializer(getApplicationContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stops Location Updates if activity is paused
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pinnedLocation, 15));
        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(17.0f);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500); // 0.5 second interval
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        currentAssigment = getRandomAssignment();
        //draw ghost assignements players
        startDraw();
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Steps", "Getting steps...");
                Blinky.getSteps(new LatLng(51.223949, 4.407599));
            }
        });
        Log.d("Movement", "Ik ben non-blocking");

        //locationupdater
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("Draggable Marker loc: ", "latitude : " + marker.getPosition().latitude + "longitude : " + marker.getPosition().longitude);
                currentPos = marker.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                if (collisionDetection.collisionDetect(marker.getPosition(), currentAssigment.getLatLng(), 10)) {
                    collisionHandler.currentAssigmentCollision();
                    collisionHandler.visitedSightsSetBoolean();
                    collisionHandler.visitedSightsPut();
                    currentAssigment = getRandomAssignment();
                    txtCurrentScore.setText(Integer.toString(player.getPlayerStats().getCurrentScore()));
                }
                for (int i = 0; i < correctedDots.size(); i++) {
                    if (collisionDetection.collisionDetect(marker.getPosition(), new LatLng(correctedDots.get(i).getLat(), correctedDots.get(i).getLon()), 8)) {
                        player.getPlayerStats().setCurrentScore(player.getPlayerStats().getCurrentScore() + 1);
                        txtCurrentScore.setText("x " + player.getPlayerStats().getCurrentScore());
                        //removerMarkers On collision
                        correctedDots.get(i).removeMarker();
                        correctedDots.remove(i);
                    }
                }
                txtCurrentHeading.setText(bearingCalc.getBearingInString(marker.getPosition().latitude, marker.getPosition().longitude, currentAssigment.getLat(), currentAssigment.getLon()));
                txtCurrentAssignment.setText(currentAssigment.getName());
                txtCurrentDistance.setText(bearingCalc.getDistance(marker.getPosition(), new LatLng(currentAssigment.getLat(), currentAssigment.getLon())));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });

        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));

        if (!success) {
            Log.e("Style failed", "Style parsing failed.");
        }

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

    //location changed
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
                drawPlayer();
                LatLng markableP = playerpos.getMarker().getPosition();
                for(int i = 0; i < assignments.size(); i++) {
                    collisionDetection.collisionDetect(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(assignments.get(i).getLat(), assignments.get(i).getLon()), 10);
                }
                collisionDetection.collisionDetect(markableP, new LatLng(currentAssigment.getLat(), currentAssigment.getLon()), 10);
                Log.d(String.valueOf(markableP.latitude), String.valueOf(markableP.longitude));

                //dots collision
                for(int i = 0; i < correctedDots.size(); i++) {
                    collisionDetection.collisionDetect(markableP, new LatLng(correctedDots.get(i).getLat(), correctedDots.get(i).getLon()), 8);
                }
                if(ghostCollide) {
                    currentAssigment = getRandomAssignment();
                    ghostCollide = false;
                    collisionHandler.ghostCollision();
                    if(player.getPlayerGameStats().getLifePoints() == 0) {
                                        getRandomAssignment();
                    }
                    txtCurrentLifePoints.setText("x " + player.getPlayerGameStats().getLifePoints());
                }

                // zooms to player
             /*   CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);*/


            }
        }
    };

    private Assignment getRandomAssignment() {
        currentAssigment = currentAssigment.getRandomAssignment(GameActivity.this, mMap, currentAssigment, assignments, circles);
        txtCurrentAssignment.setText(currentAssigment.getName());
        return currentAssigment;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        for(int i = 0; i < assignments.size(); i++) {
            if (marker.equals(assignments.get(i).getMarker()))
            {
                bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                bottomPanel.setPanelHeight(400);
                txtName.setText(assignments.get(i).getName());
                if(assignments.get(i).getWebsite() != "N/A") {
                    txtWebsite.setVisibility(View.VISIBLE);
                    txtWebsite.setText(assignments.get(i).getWebsite());
                }
                else txtWebsite.setVisibility(View.INVISIBLE);
                Picasso.get().load(assignments.get(i).getImgUrl()).into(imgSight);
                txtShortDesc.setText(assignments.get(i).getShortDescr());
                txtLongDesc.setText(assignments.get(i).getLongDescr());
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("Pushed", "pushed");
        if(marker.equals(Blinky.marker)) {
                GunHandler gunHandler = new GunHandler(Blinky, this);
                gunHandler.gunHandler();
                gunmenu.gunUpdater();
            return true;
        }
        // camera does not move anymore when dot is touched
        for(int i = 0; i < correctedDots.size(); i++) {
            if (marker.equals(correctedDots.get(i).getMarker())){
                return true; // can't move map by this
            }
        }

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


    private void initializer(Context context) {
        //objects init
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        pinnedLocation = new LatLng(51.230663, 4.407146);
        Blinky = new Ghost(new LatLng(51.229796, 4.418413));
        apiHelper = new ApiHelper();
        NavigationMenu navigationMenu = new NavigationMenu(this);
        gunmenu = new Gunmenu(this);
        dragablePlayer = new Skins();
        playerpos = new Skins();
        collisionDetection = new CollisionDetection();
        bearingCalc = new BearingCalc();
        collisionHandler = new CollisionHandler(GameActivity.this);
        circles = new ArrayList<>();
        //findview
        bottomPanel = findViewById(R.id.sliding_layout);
        txtName = findViewById(R.id.txtName);
        imgSight = findViewById(R.id.imgSight);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtShortDesc = findViewById(R.id.txtShortDesc);
        txtLongDesc = findViewById(R.id.txtLongDesc);
        txtCurrentScore = findViewById(R.id.game_txt_currentscore);
        txtCurrentLifePoints = findViewById(R.id.game_txt_currentLifePoints);
        txtCurrentAssignment = findViewById(R.id.game_txt_currentAssginment);
        txtCurrentHeading = findViewById(R.id.game_txt_currentHeading);
        txtCurrentDistance = findViewById(R.id.game_txt_currentDistance);
        //fill list
        assignments = ApiHelper.assignments;
        correctedDots= ApiHelper.dots;
        // get
        player= ApiHelper.player;
        currentAssigment = assignments.get(0);
        iin = getIntent();
        b = iin.getExtras();
        if(b!=null){
            userId = (int) b.get("userid");
            jwt = (String) b.get("jwt");
        }
        //set
        rotation = 0f;
        bottomPanel.setPanelHeight(0);
        txtCurrentScore.setText("x " + player.getPlayerStats().getCurrentScore());
        txtCurrentLifePoints.setText("x " + ApiHelper.player.getPlayerGameStats().getLifePoints());
        //skin init
        currentPos = pinnedLocation;
        Skins.SkinInit(getApplicationContext());
        //hide dev options
        if (ApiHelper.player.getId() == 21 ){
            Menu nav_menu = NavigationMenu.getNav_Menu();
            nav_menu.findItem(R.id.nav_dev).setVisible(true);}

    }

    private void startDraw(){
        //draw perimeter
        Perimeter.DrawGameFieldLine(mMap);
        //draw dots on map
        for (int i = 0; i < correctedDots.size(); i++) {correctedDots.get(i).Draw(mMap, getApplicationContext());}
        //draw player
        dragablePlayer.DrawPlayer(mMap, getApplicationContext(),100,100);
        playerpos.DrawPlayer(mMap, getApplicationContext(),100,100);
        //Blinky draw and dummy movement
        Blinky.Draw(mMap, getApplicationContext());
        //draw assignments
        for(int i = 0; i < assignments.size(); i++) {
            assignments.get(i).DrawHouses(mMap, getApplicationContext(),assignments.get(i).getName());

        }
        //hide dots on certain zoom levels
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                for (Dot item : correctedDots ) {
                    item.setMarkerVisible(item.getMarker(),false);
                }
                if (mMap.getCameraPosition().zoom <= 16){
                    for (Dot item : correctedDots ) {
                        item.setMarkerVisible(item.getMarker(),false);
                    }
                } else
                {
                    for (Dot item : correctedDots ) {
                        item.setMarkerVisible(item.getMarker(),true);
                    }
                }
            }
        });

    }

    private void drawPlayer(){
        playerpos.removeMarker();
        playerpos.DrawPlayer(mMap, getApplicationContext(),100,100);
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        playerpos.getMarker().setPosition(currentLocation);
        playerpos.setRotations(playerpos.getMarker());
        playerpos.setDraggable(playerpos.getMarker());
        Log.d("Rotation", String.valueOf( "Rotation: " + playerpos.getMarker().getRotation()));
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
         rotation = Math.round(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}

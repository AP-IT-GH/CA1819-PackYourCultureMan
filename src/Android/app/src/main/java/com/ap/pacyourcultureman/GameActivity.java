package com.ap.pacyourcultureman;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.BearingCalc;
import com.ap.pacyourcultureman.Helpers.CollisionDetection;
import com.ap.pacyourcultureman.Helpers.CollisionHandler;
import com.ap.pacyourcultureman.Helpers.GunHandler;
import com.ap.pacyourcultureman.Menus.BottomSlideMenu;
import com.ap.pacyourcultureman.Menus.Gunmenu;
import com.ap.pacyourcultureman.Menus.NavigationMenu;
import com.bhargavms.podslider.OnPodClickListener;
import com.bhargavms.podslider.PodSlider;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static com.ap.pacyourcultureman.R.drawable.openlock;

public class GameActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, SensorEventListener{

    private ArrayList<Ghost> Ghosts;
    private Ghost Blinky, Inky, Pinky, Clyde;
    private static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION = 1;
    private List<Assignment> assignments;
    private List<Dot> correctedDots;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView txtCurrentScore, txtCurrentLifePoints, txtCurrentAssignment, txtCurrentHeading, txtCurrentDistance;
    private Marker selectedMarker;
    private Location location;
    private List<Circle> circles;
    private Player player;
    private int userId,speed = 2;
    private ApiHelper apiHelper;
    private CollisionDetection collisionDetection;
    private Intent iin;
    private Bundle b;
    private String jwt,distance,bearing;
    private BearingCalc bearingCalc;
    private CollisionHandler collisionHandler;
    private Gunmenu gunmenu;
    private Handler handler;
    private GoogleMap mMap;
    private SensorManager mSensorManager;
    private Skins dragablePlayer, playerpos;
    private LatLng currentLocation;
    private boolean startDialogEnded;
    public static LatLng currentPos;
    public static Boolean ghostCollide = false;
    public static LatLng pinnedLocation;
    public static Assignment currentAssigment;
    public static float rotation;
    public static Location mLastLocation;
    BottomSlideMenu bottomSlideMenu;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab;
    private Boolean lockCam = false;
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
       /* if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);*/
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pinnedLocation, 16));
        mMap.setMinZoomPreference(16.0f);
        mMap.setMaxZoomPreference(17.0f);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500); // 0.5 second interval
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        currentAssigment = getRandomAssignment();
        //draw ghost assignements players
        startDraw();

        //locationupdater
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
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
                bottomSlideMenu.getBottomPanel().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
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
                    openAssignmentStartDialog();
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
                distance = bearingCalc.getDistance(marker.getPosition(), new LatLng(currentAssigment.getLat(), currentAssigment.getLon()));
                bearing = bearingCalc.getBearingInString(marker.getPosition().latitude, marker.getPosition().longitude, currentAssigment.getLat(), currentAssigment.getLon());
                txtCurrentHeading.setText(bearing);
                txtCurrentAssignment.setText(currentAssigment.getName());
                txtCurrentDistance.setText(distance);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });

        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));

        if (!success) {
            Log.e("Style failed", "Style parsing failed.");
        }
        progressDialog = progressDialog.show(GameActivity.this, "", "Please wait");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lockCam) {
                    lockCam = false;
                    fab.setImageResource(R.drawable.lock);
                    mMap.resetMinMaxZoomPreference();
                    mMap.setMinZoomPreference(16.0f);
                    mMap.setMaxZoomPreference(17.0f);
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                    CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16f);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);

                }
                else {
                    lockCam = true;
                    fab.setImageResource(R.drawable.openlock);
                    mMap.resetMinMaxZoomPreference();
                    mMap.setMinZoomPreference(14.0f);
                    mMap.setMaxZoomPreference(17.0f);
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    for (Dot item : correctedDots ) {
                        item.setMarkerVisible(item.getMarker(),false);
                    }
                    CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(14f);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                }
            }
        });
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
                if (collisionDetection.collisionDetect(new LatLng(location.getLatitude(), location.getLongitude()), currentAssigment.getLatLng(), 10)) {
                    collisionHandler.currentAssigmentCollision();
                    collisionHandler.visitedSightsSetBoolean();
                    collisionHandler.visitedSightsPut();
                    currentAssigment = getRandomAssignment();
                    txtCurrentScore.setText(Integer.toString(player.getPlayerStats().getCurrentScore()));
                }
                drawPlayer();
              /*  LatLng markableP = playerpos.getMarker().getPosition();
                for(int i = 0; i < assignments.size(); i++) {
                    collisionDetection.collisionDetect(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(assignments.get(i).getLat(), assignments.get(i).getLon()), 10);
                }
                collisionDetection.collisionDetect(markableP, new LatLng(currentAssigment.getLat(), currentAssigment.getLon()), 10);
                Log.d(String.valueOf(markableP.latitude), String.valueOf(markableP.longitude));

                //dots collision
                for(int i = 0; i < correctedDots.size(); i++) {
                    collisionDetection.collisionDetect(markableP, new LatLng(correctedDots.get(i).getLat(), correctedDots.get(i).getLon()), 8);
                } */
                for (int i = 0; i < correctedDots.size(); i++) {
                    if (collisionDetection.collisionDetect(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(correctedDots.get(i).getLat(), correctedDots.get(i).getLon()), 8)) {
                        player.getPlayerStats().setCurrentScore(player.getPlayerStats().getCurrentScore() + 1);
                        txtCurrentScore.setText("x " + player.getPlayerStats().getCurrentScore());
                        //removerMarkers On collision
                        correctedDots.get(i).removeMarker();
                        correctedDots.remove(i);
                    }
                }
                if(ghostCollide) {
                    currentAssigment = getRandomAssignment();
                    ghostCollide = false;
                    collisionHandler.ghostCollision();
                    if(player.getPlayerGameStats().getLifePoints() == 0) {
                                        getRandomAssignment();
                                        openAssignmentStartDialog();

                    }
                    txtCurrentLifePoints.setText("x " + player.getPlayerGameStats().getLifePoints());
                }
                txtCurrentHeading.setText(bearingCalc.getBearingInString(location.getLatitude(), location.getLongitude(), currentAssigment.getLat(), currentAssigment.getLon()));
                txtCurrentAssignment.setText(currentAssigment.getName());
                txtCurrentDistance.setText(bearingCalc.getDistance(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(currentAssigment.getLat(), currentAssigment.getLon())));
                bearing = bearingCalc.getBearingInString(location.getLatitude(), location.getLongitude(), currentAssigment.getLat(), currentAssigment.getLon());
                distance = (bearingCalc.getDistance(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(currentAssigment.getLat(), currentAssigment.getLon())));
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    openAssignmentStartDialog();
                }

                // zooms to player
                if(!lockCam) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16f);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                }
            }
        }
    };

    private Assignment getRandomAssignment() {
        currentAssigment = currentAssigment.getRandomAssignment(GameActivity.this, mMap, currentAssigment, assignments, circles);
        txtCurrentAssignment.setText(currentAssigment.getName());
        for(int i = 0; i < assignments.size(); i++) {
            if(assignments.get(i) != currentAssigment) {
                assignments.get(i).DrawHouses(mMap, getApplicationContext(),assignments.get(i).getName());
            }
            else {
                assignments.get(i).drawCurrentHouse(mMap, getApplicationContext(), assignments.get(i).getName());
            }
        }
        return currentAssigment;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        for(int i = 0; i < assignments.size(); i++) {
            if (marker.equals(assignments.get(i).getMarker()))
            {
                bottomSlideMenu.setPanel(i);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("Pushed", "pushed");
        if(marker.equals(Blinky.marker)) {
                GunHandler gunHandler = new GunHandler(Blinky, playerpos, this);
                gunHandler.gunHandler(Blinky.marker);
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
        if (bottomSlideMenu != null &&
                (bottomSlideMenu.getBottomPanel().getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)) {
            bottomSlideMenu.getBottomPanel().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else if(bottomSlideMenu.getBottomPanel().getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED && bottomSlideMenu.getBottomPanel().getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            bottomSlideMenu.getBottomPanel().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        else if(selectedMarker != null) {
            if (selectedMarker.isInfoWindowShown()){
                selectedMarker.hideInfoWindow();
            }
        } else super.onBackPressed();

    }


    private void initializer(Context context) {
        //objects init
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        pinnedLocation = new LatLng(51.230663, 4.407146);

        Blinky = new Ghost(new LatLng(51.229796, 4.418413));
        Inky = new Ghost(new LatLng(51.219429, 4.395858));
        Pinky = new Ghost(new LatLng(51.206207, 4.387096));
        Clyde = new Ghost(new LatLng(51.212186, 4.408376));
        Ghosts = new ArrayList<>();
        Ghosts.add(Blinky);
        Ghosts.add(Inky);
        Ghosts.add(Pinky);
        Ghosts.add(Clyde);

        for (int i = 0; i < Ghosts.size(); i++) {
            Ghosts.get(i).id = i;
        }

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
        bottomSlideMenu = new BottomSlideMenu(this);
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
        txtCurrentScore.setText("x " + player.getPlayerStats().getCurrentScore());
        txtCurrentLifePoints.setText("x " + ApiHelper.player.getPlayerGameStats().getLifePoints());
        //skin init
        currentPos = pinnedLocation;
        Skins.SkinInit(getApplicationContext());
        //hide dev options
        if (ApiHelper.player.getId() == 21 ){
            Menu nav_menu = NavigationMenu.getNav_Menu();
            nav_menu.findItem(R.id.nav_dev).setVisible(true);
        }
        progressDialog = new ProgressDialog(GameActivity.this);
        fab = findViewById(R.id.fab);
        fab.setAlpha(0.5f);

    }

    private void startDraw(){
        //draw perimeter
        Perimeter.DrawGameFieldLine(mMap);
        //draw dots on map
        for (int i = 0; i < correctedDots.size(); i++) {correctedDots.get(i).Draw(mMap, getApplicationContext());}
        //draw player
        dragablePlayer.drawPlayer(mMap, getApplicationContext(),100,100);
        playerpos.drawPlayer(mMap, getApplicationContext(),100,100);
        //draw assignments
        for(int i = 0; i < assignments.size(); i++) {
            assignments.get(i).DrawHouses(mMap, getApplicationContext(),assignments.get(i).getName());

        }
        //Draw Ghosts
        for (Ghost ghost:Ghosts) {
            ghost.Draw(mMap, getApplicationContext());
        }

        //hide dots on certain zoom levels
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                for (Dot item : correctedDots ) {
                    item.setMarkerVisible(item.getMarker(),false);
                }
                if (mMap.getCameraPosition().zoom <= 15.9){
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
        playerpos.drawPlayer(mMap, getApplicationContext(),100,100);
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
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
    protected void onRestart() {
        super.onRestart();
        txtCurrentLifePoints.setText("x " + ApiHelper.player.getPlayerGameStats().getLifePoints());
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private boolean openAssignmentStartDialog(){

        final Dialog dialog1 = new Dialog(GameActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_startassignment);
        final TextView txt_distance = dialog1.findViewById(R.id.dialog_txt_currentDistance);
        final TextView txt_bearing = dialog1.findViewById(R.id.dialog_txt_currentHeading);
        final TextView txt_assignment = dialog1.findViewById(R.id.txt_assignmentname);
        final TextView txt_speed = dialog1.findViewById(R.id.dialog_txt_currentSpeed);
        final PodSlider podSlider = (PodSlider) dialog1.findViewById(R.id.pod_slider);
        final Button btnGo = dialog1.findViewById(R.id.dialog_btn_GO);
        startDialogEnded = false;
        podSlider.setPodClickListener(new OnPodClickListener() {
            @Override
            public void onPodClick(int position) {
                speed = SetSpeed(position);        handler = new Handler();
                Log.d("speed",Integer.toString(speed));
                txt_speed.setText(addKm(speed));
                for (Ghost ghost:Ghosts) {
                    ghost.setSpeed((int)(speed/3.6));
                }

            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogEnded = true;
                dialog1.dismiss();
                openCountDownDialog();
            }

        });
        txt_assignment.setText(currentAssigment.getName());
        txt_distance.setText(distance);
        txt_bearing.setText(bearing);
        txt_speed.setText(addKm(speed));
        //dialog1.setCancelable(false);

        dialog1.show();
        return startDialogEnded;

    }
    private void openCountDownDialog(){
        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_counter);
       // dialog.setCancelable(false);
        final TextView txt_counter = dialog.findViewById(R.id.txt_counter);
        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {

                txt_counter.setText(Long.toString((millisUntilFinished / 1000)-1));

                if(millisUntilFinished <= 1000){
                    txt_counter.setText("GO!");
                    handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Steps", "Getting steps...");
                            for (Ghost ghost:Ghosts) {
                                ghost.getSteps(ApiHelper.assignments.get(1).getLatLng());
                            }
                        }
                    });
                    Log.d("Movement", "Ik ben non-blocking");
                }
            }
            public void onFinish() {

                dialog.dismiss();
            }
        }.start();
        dialog.show();

    }
    private String addKm(int speed){
        String speedStr = Integer.toString(speed);
        return speedStr + "km/h";
    }
    private int SetSpeed(int position){
        int speed = 5;
        switch(position) {
            case 0:
                speed = 2;
                break;
            case 1:
                speed = 3;
                break;
            case 2:
                speed = 4;
                break;
            case 3:
                speed = 5;
                break;
            case 4:
                speed = 6;
                break;

        }
        return speed;
    }


}

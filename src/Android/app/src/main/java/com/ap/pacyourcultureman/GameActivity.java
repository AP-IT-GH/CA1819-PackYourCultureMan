package com.ap.pacyourcultureman;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import static com.ap.pacyourcultureman.Helpers.getDotsBetween2Points.GetDotsBetweenAanB;

public class GameActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Ghost Blinky;
    private Bitmap scaledPacman;
    private static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION = 1;
    List<Assignment> assignments = ApiHelper.assignments;
    List<Dot>   correctedDots = ApiHelper.dots;
    Location mLastLocation;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    static  public Assignment currentAssigment;
    SlidingUpPanelLayout bottomPanel;
    TextView txtName, txtWebsite, txtShortDesc, txtLongDesc, txtCurrentScore, txtCurrentLifePoints, txtCurrentAssignment, txtCurrentHeading, txtCurrentDistance;
    ImageView imgSight;
    Marker selectedMarker;
    Location location;
    List<Circle> circles;
    Marker perth;
    Player player = ApiHelper.player;
    int userId;
    ApiHelper apiHelper;
    CollisionDetection collisionDetection;
    Intent iin;
    Bundle b;
    String jwt;
    static LatLng currentPos;
    static Boolean ghostCollide = false;
    BearingCalc bearingCalc;
    CollisionHandler collisionHandler;
    Gunmenu gunmenu;
    static final LatLng PERTH = new LatLng(51.230663, 4.407146);
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializer(getApplicationContext());
        Blinky = new Ghost(new LatLng(51.229796, 4.418413));
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
        DrawGameFieldLine();
        //draw dots on map
        for (int i = 0; i < correctedDots.size(); i++) {correctedDots.get(i).Draw(mMap, getApplicationContext());}
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //Blinky draw and dummy movement
        Blinky.Draw(mMap, getApplicationContext());
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Steps", "Getting steps...");
                Blinky.getSteps(new LatLng(51.223949, 4.407599));
            }
        });
        Log.d("Movement", "Ik ben non-blocking");

        for(int i = 0; i < assignments.size(); i++) {
            assignments.get(i).DrawHouses(mMap, getApplicationContext(),assignments.get(i).getName());
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
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
                if(collisionDetection.collisionDetect(marker.getPosition(), currentAssigment.getLatLng(), 10)){
                    collisionHandler.currentAssigmentCollision();
                    collisionHandler.visitedSightsSetBoolean();
                    collisionHandler.visitedSightsPut();
                    currentAssigment = getRandomAssignment();
                    txtCurrentScore.setText(Integer.toString(player.getPlayerStats().getCurrentScore()));
                }
                for(int i = 0; i < correctedDots.size(); i++) {
                    if(collisionDetection.collisionDetect(marker.getPosition(), new LatLng(correctedDots.get(i).getLat(), correctedDots.get(i).getLon()), 8)) {
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
        perth = mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledPacman)));

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
                    collisionDetection.collisionDetect(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(assignments.get(i).getLat(), assignments.get(i).getLon()), 10);

                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CircleOptions circleOptions = new CircleOptions();
                LatLng markable = perth.getPosition();
                collisionDetection.collisionDetect(markable, new LatLng(currentAssigment.getLat(), currentAssigment.getLon()), 10);
                Log.d(String.valueOf(markable.latitude), String.valueOf(markable.longitude));

                //dots collision
                for(int i = 0; i < correctedDots.size(); i++) {
                    collisionDetection.collisionDetect(markable, new LatLng(correctedDots.get(i).getLat(), correctedDots.get(i).getLon()), 5);
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
            }
        }
    };
    private Assignment getRandomAssignment() {
        currentAssigment = currentAssigment.getRandomAssignment(GameActivity.this, mMap, currentAssigment, assignments, circles);
        txtCurrentAssignment.setText(currentAssigment.getName());
        return currentAssigment;
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
        apiHelper = new ApiHelper();
        bottomPanel = findViewById(R.id.sliding_layout);
        bottomPanel.setPanelHeight(0);
        txtName = findViewById(R.id.txtName);
        imgSight = findViewById(R.id.imgSight);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtShortDesc = findViewById(R.id.txtShortDesc);
        txtLongDesc = findViewById(R.id.txtLongDesc);
        txtCurrentScore = findViewById(R.id.game_txt_currentscore);
        txtCurrentScore.setText("x " + player.getPlayerStats().getCurrentScore());
        txtCurrentLifePoints = findViewById(R.id.game_txt_currentLifePoints);
        txtCurrentLifePoints.setText("x " + ApiHelper.player.getPlayerGameStats().getLifePoints());
        txtCurrentAssignment = findViewById(R.id.game_txt_currentAssginment);
        txtCurrentHeading = findViewById(R.id.game_txt_currentHeading);
        txtCurrentDistance = findViewById(R.id.game_txt_currentDistance);
        NavigationMenu navigationMenu = new NavigationMenu(this);
        gunmenu = new Gunmenu(this);
        collisionDetection = new CollisionDetection();
        bearingCalc = new BearingCalc();
        collisionHandler = new CollisionHandler(GameActivity.this);
        currentAssigment = assignments.get(0);
        circles = new ArrayList<>();
        iin = getIntent();
        b = iin.getExtras();
        currentPos = PERTH;
        Bitmap pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(),R.drawable.man, null));
        scaledPacman = Bitmap.createScaledBitmap(pacman, 150, 150, false);
        if(b!=null){
            userId = (int) b.get("userid");
            jwt = (String) b.get("jwt");
        }
        if (ApiHelper.player.getId() <5 ){
            Menu nav_menu = NavigationMenu.getNav_Menu();
            nav_menu.findItem(R.id.nav_dev).setVisible(true);}
    }

    private  void DrawGameFieldLine(){
        mMap.addPolygon(new PolygonOptions().add(
                new LatLng(51.208187, 4.384678), new LatLng(51.207181, 4.386604),
                new LatLng(51.205057, 4.388015), new LatLng(51.204835, 4.388629),
                new LatLng(51.204428, 4.388991), new LatLng(51.210132, 4.406642),
                new LatLng(51.214943, 4.413262), new LatLng(51.215252, 4.413283),
                new LatLng(51.216186, 4.414217), new LatLng(51.220990, 4.416567),
                new LatLng(51.228645, 4.413866), new LatLng(51.229040, 4.418705),
                new LatLng(51.229816, 4.418737), new LatLng(51.229797, 4.413367),
                new LatLng(51.230891, 4.413121), new LatLng(51.231049, 4.412558),
                new LatLng(51.231083, 4.408443), new LatLng(51.231325, 4.407488),
                new LatLng(51.231313, 4.405128), new LatLng(51.234400, 4.405026),
                new LatLng(51.234467, 4.403918), new LatLng(51.234373, 4.403333),
                new LatLng(51.233599, 4.403078), new LatLng(51.231142, 4.402863),
                new LatLng(51.230208, 4.402348), new LatLng(51.229673, 4.401967),
                new LatLng(51.227898, 4.401870), new LatLng(51.226393, 4.400411),
                new LatLng(51.222813, 4.398169), new LatLng(51.223087, 4.397072),
                new LatLng(51.222943, 4.396975), new LatLng(51.222353, 4.397187),
                new LatLng(51.221862, 4.396830), new LatLng(51.221724, 4.396492),
                new LatLng(51.221487, 4.396505), new LatLng(51.221343, 4.397210),
                new LatLng(51.218344, 4.395123), new LatLng(51.215286, 4.392913),
                new LatLng(51.210386, 4.387924), new LatLng(51.210306, 4.387794),
                new LatLng(51.208187, 4.384678)).strokeColor(Color.RED).strokeWidth(7));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        txtCurrentLifePoints.setText("x " + ApiHelper.player.getPlayerGameStats().getLifePoints());
    }
}

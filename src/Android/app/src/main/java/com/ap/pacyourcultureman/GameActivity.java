package com.ap.pacyourcultureman;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
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

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.CollisionDetection;
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
    TextView txtName, txtWebsite, txtShortDesc, txtLongDesc, txtCurrentScore;
    ImageView imgSight;
    Marker selectedMarker;
    Location location;
    Circle Circle;
    Marker perth;
    int currentScore;
    CollisionDetection collisionDetection;
    Intent iin;
    Bundle b;
    int userId;
    String jwt;
    static final LatLng PERTH = new LatLng(51.230663, 4.407146);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        currentScore = 0;
        NavigationView navigationView = (NavigationView) findViewById(R.id.menu);
        bottomPanel = findViewById(R.id.sliding_layout);
        bottomPanel.setPanelHeight(0);
        txtName = findViewById(R.id.txtName);
        imgSight = findViewById(R.id.imgSight);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtShortDesc = findViewById(R.id.txtShortDesc);
        txtLongDesc = findViewById(R.id.txtLongDesc);
        txtCurrentScore = findViewById(R.id.game_txt_currentscore);
        txtCurrentScore.setText("x " + currentScore);
        collisionDetection = new CollisionDetection();

        iin = getIntent();
        b = iin.getExtras();
        if(b!=null){
            userId = (int) b.get("userid");
            jwt = (String) b.get("jwt");
        }

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
                    case R.id.nav_sights:
                        intent = new Intent(getBaseContext(), Sights.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_stats:
                        intent = new Intent(getBaseContext(),StatsPage.class);
                        intent.putExtra("userid",userId);
                        intent.putExtra("jwt",jwt);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(getBaseContext(),Settings.class);
                        Log.e("jwt",jwt);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        Blinky = new Ghost();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
      //  hardcodedAssigments();
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
        mLocationRequest.setInterval(2000); // 2 second interval
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        Blinky.Draw(mMap, getApplicationContext());
        Blinky.FollowPath(new LatLng(1,1), new LatLng(1,1));
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(51.217065, 4.397200));
        for(int i = 0; i < assignments.size(); i++) {
           Marker mark;
           LatLng assigmentMarker = new LatLng(assignments.get(i).lat, assignments.get(i).lon);
           mark = mMap.addMarker(new MarkerOptions().position(assigmentMarker).title(assignments.get(i).name));
           assigmentMarkers.add(mark);
        }
        //dots
        for (int i = 0; i < dots.size(); i++) {dots.get(i).Draw(mMap, getApplicationContext());}
        //dots
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
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                if(collisionDetection.collisionDetect(marker.getPosition(), currentAssigment.latLng, 10)){
                    Log.d("assigmentHit", "assigmentHit");
                }
                for(int i = 0; i < dots.size(); i++) {
                    if(collisionDetectMarker(marker.getPosition(), new LatLng(dots.get(i).getLat(), dots.get(i).getLon()), 0.00007)) {
                        Log.v("Dot", "dot hit");
                        currentScore++;
                        txtCurrentScore.setText("x " + currentScore);
                    }
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
        for (int i = 0; i<=90;i++) {
            currentAssigment = getRandomAssignment();
            Log.d("Assignment", currentAssigment.name);
        }
        perth = mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .draggable(true));

        /*PolylineOptions rectOptions2 = new PolylineOptions()
                .add(new LatLng(currentAssigment.lat - 0.0001, currentAssigment.lon - 0.0001))
                .add(new LatLng(currentAssigment.lat - 0.0001, currentAssigment.lon + 0.0001))  // North of the previous point, but at the same longitude
                .add(new LatLng(currentAssigment.lat + 0.0001, currentAssigment.lon + 0.0001))  // Same latitude, and 30km to the west
                .add(new LatLng(currentAssigment.lat + 0.0001, currentAssigment.lon - 0.0001))  // Same longitude, and 16km to the south
                .add(new LatLng(currentAssigment.lat - 0.0001, currentAssigment.lon - 0.0001)); // Closes the polyline.

// Get back the mutable Polyline
        Polyline polyline2 = mMap.addPolyline(rectOptions2);
        */
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
                    collisionDetectMarker(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(assignments.get(i).lat, assignments.get(i).lon), 0.000100);

                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CircleOptions circleOptions = new CircleOptions();
                LatLng markable = perth.getPosition();
                collisionDetectMarker(markable, new LatLng(currentAssigment.lat, currentAssigment.lon), 0.0001);
                Log.d(String.valueOf(markable.latitude), String.valueOf(markable.longitude));

                //dots collision
                for(int i = 0; i < dots.size(); i++) {
                    collisionDetectMarker(markable, new LatLng(dots.get(i).getLat(), dots.get(i).getLon()), 0.000100);
                }

/*                circleOptions.center(latLng);
                circleOptions.radius(20);
                circleOptions.strokeColor(Color.BLUE);
                circleOptions.fillColor(0x30ff0000);
                circleOptions.strokeWidth(2);*/

                //mMap.addCircle(circleOptions);

                //move map camera
              //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }
    };
    /*private void hardcodedAssigments() {
        assignments = new ArrayList<>();
        assignments.add(new Assignment("Red Star Line Museum", "https://www.redstarline.be/nl", 51.2329543, 4.4034171, "Museum exhibition based on the 3 million who emigrated to America using this historic shipping line.", "The Red Star Line Museum does not have a typical museum collection. Do not expect endless rows of glass cabinets full of 19th-century trinkets or ship’s parts. No, the Red Star Line Museum has a very unusual collection. Above all, it collects and archives stories. Audiovisual testimonies and written documents. The museum is still looking for stories from the period between 1873 and 1935. \n" +
                "Of course, you will also find art in its more traditional form. For example, the Red Star Line and Antwerp, as a migration hub, inspired artists like Eugeen Van Mieghem and Louis van Engelen.\n" +
                "What makes this museum even more special is the fact that it adopts a very modern approach in the original Red Star Line buildings. The port warehouses that were used for passengers’ administrative and medical checks are the very highlight of the collection.\n" +
                "More than a museum\n" +
                "The Red Star Line Museum is not just a museum. The observation tower that rises above the warehouses, in the shape of a ship's smokestack, affords an amazing panoramic view. And you can visit The Shed, a cozy café and nice museum shop, up to an hour after the museum closes.\n" +
                "Be touched by the testimonies of people who have boarded the Red Star Line - for pleasure, for business or in the hope to find a better life - and enjoy a unique view of the Scheldt and the centre of Antwerp. And sit down and talk about it afterwards in The Shed.\n" +
                "After a visit to the Red Star Line Museum, it is definitely worth walking through the trendy Eilandje and enjoy a meal at one of the many restaurants located nearby. From Eilandje, you can also see the Port House, famously designed by the world-renowned architect Zaha Hadid Architects.\n",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Red_Star_Line_Museum.jpg/220px-Red_Star_Line_Museum.jpg"));
        assignments.add(new Assignment("Museum aan de stroom", "www.mas.be", 51.2289219, 4.4047576, "Striking red sandstone museum with high-tech exhibitions exploring Antwerp's place in the world.", "A river runs through it\n" +
                "Antwerp is the city on the Scheldt, the city on the river, facilitating encounters and exchanges between people from all over the world for several centuries. The MAS museum collects proof of these encounters, using this to tell new stories. About the city, the river and the port on the one hand. But also about the world. The MAS has many facets, and is teeming with stories and surprises.\n" +
                "From the port to life and death\n" +
                "The MAS has a phenomenally large collection, which to date comprises about 500,000 items, including artworks and utensils. New objects are constantly being added to the collection.\n" +
                "The museum uses its entire collection to weave a new narrative, based on five universal themes, on just as many floors. The MAS takes a closer look at power politics and world ports. At how food shaped and will shape today’s metropolises in the past, present and future. And at life and death, of people and gods, in the upper and under world. Moreover, the third floor and the walking boulevard host some fascinating and highly diverse temporary exhibitions.\n" +
                "But above all, the MAS excels at connecting all these stories. This is not your typical museum, where you walk from display case to display case. Instead all the stories engage with each other, thanks to the way in which the floors have been arranged and are connected with each other. Ensuring you understand Antwerp and the world a bit better at the end of your visit.\n",
                "https://archello.imgix.net/images/2015/03/16/2099SB05.1506066063.5785.jpg?fit=crop&h=518&w=414"));
        assignments.add(new Assignment("Saint Paul’s Church", "http://www.sint-paulusparochie.be/", 51.22414, 4.4006885, "This restored Gothic church with a Baroque tower houses works by Antwerp painters Rubens & van Dyck.", "Originally, Saint Paul’s Church was part of a large Dominican abbey. It was consecrated in 1571 as a replacement for another church. A new Baroque steeple was built after a ravaging fire destroyed the church in 1679.\n" +
                "The church's striking interior hosts fifty paintings by renowned Antwerp masters, Rubens, Van Dyck and Jordaens, over 200 sculptures, beautiful Baroque altars and sculpted church furniture, widely considered to be amongst the most beautiful in the world. The organ was built in the 17th century, but has been repeatedly restored and expanded.\n" +
                "An eye-catching feature is the 18th century Calvary with sixty life-sized figures, next to the church on the corner of Veemarkt and Zwartzustersstraat.\n",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Sint-Pauluskerk_op_de_Veemarkt_in_Antwerpen.jpg/260px-Sint-Pauluskerk_op_de_Veemarkt_in_Antwerpen.jpg"));
        assignments.add(new Assignment("Stadswaag", "N/A", 51.2239475, 4.4050828, "At the Stadswaag, merchants had their goods weighed - until a bombing raided the city map. Then hippies and punks took over this square and it became the nightlife district.", "At the Stadswaag, merchants had their goods weighed - until a bombing raided the city map. Then hippies and punks took over this square and it became the nightlife district. Today families here live fraternally alongside blocking students.\n" +
                "In the past, the goods of merchants were weighed here, but a Zeppelin bombardment unfortunately caused a total disappearance of the actual weigh house. In the sixties this square was the entertainment district par excellence. Today it is not only populated by terrace people and students, but many families have also found a permanent home here.\n",
                "http://www.aviewoncities.com/img/antwerp/kvefl141s.jpg"));
    }*/
    private Assignment getRandomAssignment() {
        if(Circle != null) {
            Circle.remove();
        }
        Random rand = new Random();
        int n = rand.nextInt(assignments.size());
        if(assignments.get(n) == currentAssigment) {
            n = rand.nextInt(assignments.size());
        }
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(assignments.get(n).lat, assignments.get(n).lon));
                circleOptions.radius(10);
                circleOptions.strokeColor(Color.YELLOW);
                circleOptions.fillColor(0x30ff0000);
                circleOptions.strokeWidth(2);
                Circle = mMap.addCircle(circleOptions);
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
    private boolean collisionDetectMarker(LatLng latLng1, LatLng latLng2, Double hitboxSize) {
        if(latLng1.latitude > latLng2.latitude - hitboxSize && latLng1.latitude < latLng2.latitude + hitboxSize && latLng1.longitude > latLng2.longitude - hitboxSize && latLng1.longitude < latLng2.longitude + hitboxSize) {
          //  Log.d("Hit", "fucking hit motherfucker ");
            return true;
            }
            else return false;
    }
}

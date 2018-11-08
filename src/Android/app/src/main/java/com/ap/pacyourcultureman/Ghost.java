package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;

import com.ap.pacyourcultureman.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.ap.pacyourcultureman.GameActivity.getBitmapFromDrawable;

public class Ghost {
    private LatLng location;
    private int id;

    public Ghost(){

    }

    public int setColor(){
        return 1;
    }
    // we moeten hier waarschijnlijk ook een soort van AI programmeren anders gaan de spookjes
    // op 1 rechte lijn terecht komen achter de speler.

    public void Draw(GoogleMap mMap, Context context){
        int height = 80;
        int width = 80;
        Bitmap bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_launcher_foreground, null));
        Bitmap smallerblinky = Bitmap.createScaledBitmap(bitmap, width, height, false);

        LatLng spookyloc = new LatLng(51.230108, 4.418516);
        Marker spookymarker = mMap.addMarker(new MarkerOptions()
                .position(spookyloc)
                .icon(BitmapDescriptorFactory.fromBitmap(smallerblinky)));
    }
}
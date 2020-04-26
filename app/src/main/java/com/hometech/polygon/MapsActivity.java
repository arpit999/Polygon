package com.hometech.polygon;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AbstractMapActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> points = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();

    private Polygon polygon;
    public final static String MAP_OPTION = "map_option";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readyToGo()) {
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            if (savedInstanceState == null) {
                mapFragment.getMapAsync(this);
            }

            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044,
                        -73.98180484771729));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.setIndoorEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                marker.setTag(latLng);
                markerList.add(marker);
                points.add(latLng);
                drawPolygon(points);
            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                System.out.println("Before update the list");
                System.out.println(Arrays.toString(points.toArray()));
                System.out.println(Arrays.toString(markerList.toArray()));
                updateMarkerLocation(marker, false);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                updateMarkerLocation(marker, true);
                System.out.println("After update the list");
                System.out.println(Arrays.toString(points.toArray()));
                System.out.println(Arrays.toString(markerList.toArray()));

            }
        });

    }

    public void closePolygon(View view) {

    }

    public void newPolygon(View view) {

        points.clear();
        markerList.clear();

        mMap.clear();
    }

    private void updateMarkerLocation(Marker marker, boolean calculate) {
        LatLng latLng = (LatLng) marker.getTag();
        int position = points.indexOf(latLng);
        points.set(position, marker.getPosition());
        marker.setTag(marker.getPosition());
        drawPolygon(points);

    }

    private void drawPolygon(List<LatLng> latLngList) {
        if (polygon != null) {
            polygon.remove();
        }
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(latLngList);
        polygon = mMap.addPolygon(polygonOptions);
    }


}

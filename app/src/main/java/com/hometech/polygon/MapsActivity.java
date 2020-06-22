package com.hometech.polygon;

import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AbstractMapActivity implements OnMapReadyCallback {

    // Map a marker id to its corresponding list (represented by the root marker id)
    HashMap<String, String> markerToList = new HashMap<>();

    // A list of markers for each polygon (designated by the marker root).
    HashMap<String, List<Marker>> polygonMarkers = new HashMap<>();

    // A list of polygon points for each polygon (designed by the marker root).
    HashMap<String, List<LatLng>> polygonPoints = new HashMap<>();

    // List of polygons (designated by marker root).
    HashMap<String, Polygon> polygons = new HashMap<>();

    // The active polygon (designated by marker root) - polygon added to.
    String markerListKey;

    // Flag used to record when the 'New Polygon' button is pressed.  Next map
// click starts a new polygon.
    boolean newPolygon = false;

    private GoogleMap mMap;
    private List<LatLng> points = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();

    PolygonOptions polygonOptions;

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

                // Special case for very first marker.
                if (polygonMarkers.size() == 0) {
                    polygonMarkers.put(marker.getId(), new ArrayList<Marker>());
                    // only 0 or 1 polygons so just add it to new one or existing one.
                    markerList = new ArrayList<>();
                    points = new ArrayList<>();
                    polygonMarkers.put(marker.getId(), markerList);
                    polygonPoints.put(marker.getId(), points);
                    markerListKey = marker.getId();
                }

                if (newPolygon) {
                    newPolygon = false;
                    markerList = new ArrayList<>();
                    points = new ArrayList<>();
                    polygonMarkers.put(marker.getId(),markerList);
                    polygonPoints.put(marker.getId(),points);
                    markerListKey = marker.getId();
                }

                markerList.add(marker);
                points.add(latLng);
                markerToList.put(marker.getId(), markerListKey);

                drawPolygon(markerListKey, points);
            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                updateMarkerLocation(marker, false);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                updateMarkerLocation(marker, true);

            }
        });

    }


    public void newPolygon(View view) {
        newPolygon = true;
    }

    private void updateMarkerLocation(Marker marker, boolean calculate) {

        // Use the marker to figure out which polygon list to use...
        List<LatLng> pts = polygonPoints.get(markerToList.get(marker.getId()));

        // This is much the same except use the retrieved point list.
        LatLng latLng = (LatLng) marker.getTag();
        int position = pts.indexOf(latLng);
        pts.set(position, marker.getPosition());
        marker.setTag(marker.getPosition());
        drawPolygon(markerToList.get(marker.getId()), pts);

    }


    private void drawPolygon(String mKey, List<LatLng> latLngList) {

        // Use the existing polygon (if any) for the root marker.
        Polygon polygon = polygons.get(mKey);
        if (polygon != null) {
            polygon.remove();
        }
        polygonOptions = new PolygonOptions();
        polygonOptions.addAll(latLngList);
        polygon = mMap.addPolygon(polygonOptions);

        // And update the list for the root marker.
        polygons.put(mKey, polygon);
    }

}

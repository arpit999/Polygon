package com.hometech.polygon;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class Polygons {

    private List<LatLng> points;
    private int polygonNumber;
    private PolygonOptions polygonOptions;


    public Polygons(List<LatLng> points, int polygonNumber, PolygonOptions polygonOptions) {
        this.points = points;
        this.polygonNumber = polygonNumber;
        this.polygonOptions = polygonOptions;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public int getPolygonNumber() {
        return polygonNumber;
    }

    public void setPolygonNumber(int polygonNumber) {
        this.polygonNumber = polygonNumber;
    }

    public PolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    public void setPolygonOptions(PolygonOptions polygonOptions) {
        this.polygonOptions = polygonOptions;
    }

    @Override
    public String toString() {
        return "Polygons{" +
                "points=" + points +
                ", polygonNumber=" + polygonNumber +
                ", polygonOptions=" + polygonOptions +
                '}';
    }
}

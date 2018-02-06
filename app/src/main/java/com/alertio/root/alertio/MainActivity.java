package com.alertio.root.alertio;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private FirebaseDatabase fbDataBase;
    private DatabaseReference FrameDBRef;
    private static final String TAG = "MainActivity";
    Float lat; Float lng; String TAGG; String desc;

    Map <String, Double> latm = new HashMap<String, Double>();
    Map <String, Double> lngm = new HashMap<String, Double>();
    Map <String, String> tagm = new HashMap<String, String>();
    Map <String, String> descm = new HashMap<String, String>();
    Map <String, String> idm = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoic21hbW1lcmkiLCJhIjoiY2pkYWgwbnhhMG5jcTMzcDcwcjQxZGwzOCJ9.Dr4cBXkdzF3OOzZgX9kZGw");
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        fbDataBase = FirebaseDatabase.getInstance();
        FrameDBRef = fbDataBase.getReference("Alerts");

        FrameDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dev : dataSnapshot.getChildren()) {
                    if(dev.getValue() instanceof Map){
                        Map<String, Float> list = (Map<String,Float>) dev.getValue();
                        Set<String> keys = list.keySet();
                        for(String key : keys){
                            System.out.println(key);
                            if("lat".equals(key))  latm.put(dev.getKey(), Double.parseDouble(dev.child("lat").getValue(String.class)));
                            if("lng".equals(key))  lngm.put(dev.getKey(), Double.parseDouble(dev.child("lng").getValue(String.class)));;
                            if("TAG".equals(key))  tagm.put(dev.getKey(), dev.child("TAG").getValue(String.class));
                            if("desc".equals(key)) descm.put(dev.getKey(), dev.child("desc").getValue(String.class));
                        }
                        idm.put(dev.getKey(), "miaou");
                    }
                }

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        // One way to add a marker view
                        Set<String> markerKeys = idm.keySet();

                        for(String marker : markerKeys){
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latm.get(marker),lngm.get(marker)))
                                    .title(tagm.get(marker))
                                    .snippet(descm.get(marker))
                            );
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}

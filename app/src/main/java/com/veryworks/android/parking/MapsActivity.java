package com.veryworks.android.parking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Remote.Callback {

    private GoogleMap mMap;
    private String url = "http://openapi.seoul.go.kr:8088/4c425976676b6f643437665377554c/json/SearchParkingInfoRealtime/1/500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // 1. 공영주차장 마커 전체를 화면에 출력
        Remote remote = new Remote();
        remote.getData(this);

        // 2. 중심점을 서울로 이동
        //서울시청 위도(Latitude): 37.566696, 경도(Longitude): 126.977942

        LatLng seoul = new LatLng(37.566696, 126.977942);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,10));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void call(String jsonString) {
        try {
            // MainActivity의 화면에 뭔가를 세팅해주면, Remote 에서 이 함수를 호출해 준다.
            // 1. json String 전체를 JSONObject 로 변환
            JSONObject jsonObject = new JSONObject(jsonString);
            // 2. JSONObject 중에 최상위의 object 를 꺼낸다
            JSONObject rootObject = jsonObject.getJSONObject("SearchParkingInfoRealtime");
            // 3. 사용하려는 주차장 정보(복수개)들을 JSONArray 로 꺼낸다
            //    이 데이터를 rootObject 바로 아래에 실제 정보가 있지만 계층구조상 더 아래에 존재할 수도 있다
            JSONArray rows = rootObject.getJSONArray("row");
            int arrayLength = rows.length();

            for (int i = 0; i < arrayLength; i++) {
                JSONObject park = rows.getJSONObject(i);
                double lat = getDouble(park,"LAT");
                double lng = getDouble(park,"LNG");
                LatLng parking = new LatLng(lat, lng);

                int capacity = getInt(park, "CAPACITY");
                int current = getInt(park, "CUR_PARKING");
                int space = capacity - current;

                mMap.addMarker(new MarkerOptions().position(parking).title(space + "/" + capacity));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private double getDouble(JSONObject obj, String key){
        double result = 0;
        try {
            result = obj.getDouble(key);
        }catch(Exception e){

        }
        return result;
    }

    private int getInt(JSONObject obj, String key){
        int result = 0;
        try {
            result = obj.getInt(key);
        }catch(Exception e){

        }
        return result;
    }
}

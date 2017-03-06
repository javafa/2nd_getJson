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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Remote.Callback {

    private GoogleMap mMap;
    private String url = "http://openapi.seoul.go.kr:8088/4c425976676b6f643437665377554c/xml/SearchParkingInfoRealtime/1/500";

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
    public void call() {
        // MainActivity의 화면에 뭔가를 세팅해주면, Remote 에서 이 함수를 호출해 준다.

        while(true) {
            LatLng parking = new LatLng(37, 126);
            mMap.addMarker(new MarkerOptions().position(parking).title("주차가능대수/총대수"));
        }
    }
}

package com.defh.mapstest;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        
        GPSTrack gps = new GPSTrack(MainActivity.this);
		double latitude = 0, longitude = 0;
		
		if(gps.canGetLocation())
		{
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}
		else
		{
			gps.showSettingsAlert();
		}
		

        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        IMapController mapController = map.getController();
        mapController.setZoom(16);
        mapController.setCenter(startPoint);
        
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        map.getOverlays().add(startMarker);

        map.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}

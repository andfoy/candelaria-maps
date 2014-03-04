package com.defh.mapstest;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private boolean landmarksActive = true;
	private MapView map;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.ThreadPolicy policy = new
		StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
				StrictMode.setThreadPolicy(policy);
		
		LocationFileReader reader = new LocationFileReader("https://raw.github.com/andfoy/candelaria-maps/master/test"); 

		map = (MapView) findViewById(R.id.map);
		
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
        try {
        	int i;
			List<String[]> landmarks = reader.getFile();
			for(i = 0; i < landmarks.size(); i++)
			{
				String[] currentLandmark = landmarks.get(i);
				GeoPoint locationPoint = new GeoPoint((Double.parseDouble(currentLandmark[0])), Double.parseDouble(currentLandmark[1]));
				makeToast("Loading Landmark: "+currentLandmark[3]+"Lat: "+currentLandmark[0]+"Long: "+currentLandmark[1]);
				Landmarks landmarkPoint = new Landmarks(locationPoint, currentLandmark[3], Integer.parseInt(currentLandmark[2])); 
				boolean cond = landmarkPoint.drawLandmark(landmarksActive, map);
				map.invalidate();
				makeToast("Landmark draw: "+cond);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		//Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		//startActivity(intent);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    		startActivity(intent);
	            return true;
	        case R.id.display_landmarks:
	        	landmarksActive = !landmarksActive;
	        	//landmark.drawLandmark(landmarksActive, map, Landmarks.landmark1);
                makeToast("Status is: "+landmarksActive);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);    
	    }
	}
	
	public void makeToast(String message)
	{
		Context context = getApplicationContext();
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, message, duration);
    	toast.show();
	}
	
	

}

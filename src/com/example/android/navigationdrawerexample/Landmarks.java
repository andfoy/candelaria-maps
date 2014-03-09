package com.example.android.navigationdrawerexample;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.graphics.drawable.Drawable;

public class Landmarks {
	
	private GeoPoint location;
	private String name;
	int id;
	
	public Landmarks(GeoPoint coordinates, String locName, int idNumber)
	{
		location = coordinates;
		name = locName;
		id = idNumber;
	}
	
	//public static final GeoPoint landmark1 = new GeoPoint(4.5979799, -74.0760842);
	
	public boolean drawLandmark(boolean state, MapView map)
	{
		Drawable nodeIcon = map.getResources().getDrawable(R.drawable.marker_node);	
		Marker startMarker = new Marker(map);
		startMarker.setPosition(location);
		if(state)
		{
			startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
			startMarker.setIcon(nodeIcon);
			map.getOverlays().add(startMarker);
			map.invalidate();
			return true;
			
		}
		else
		{
			startMarker.remove(map);
			map.invalidate();
			return false;
		}
	}
	
	
	

}

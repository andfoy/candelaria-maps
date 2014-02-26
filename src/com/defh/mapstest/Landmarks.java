package com.defh.mapstest;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.graphics.drawable.Drawable;

public class Landmarks {
	
	
	public static final GeoPoint landmark1 = new GeoPoint(4.5979799, -74.0760842);
	
	public void drawLandmark(boolean state, MapView map, GeoPoint location)
	{
		Drawable nodeIcon = map.getResources().getDrawable(R.drawable.marker_node);	
		Marker startMarker = new Marker(map);
		startMarker.setPosition(location);
		if(state)
		{
			startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
			startMarker.setIcon(nodeIcon);
			map.getOverlays().add(startMarker);
		}
		else
		{
			startMarker.remove(map);
		}
		map.invalidate();
	}
	

}

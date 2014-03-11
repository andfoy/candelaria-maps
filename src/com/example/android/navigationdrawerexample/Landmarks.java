package com.example.android.navigationdrawerexample;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Landmarks {
	
	private GeoPoint location;
	private String name;
 	private int id;
 	private String category;
 	private Activity activity;
 	Button btn;
	
	public Landmarks(Activity act, GeoPoint coordinates, String locName, int idNumber, String cat)
	{
 		activity = act;
		location = coordinates;
		name = locName;
		id = idNumber;
		category = cat;
	}
	
	public GeoPoint getLocation()
	{
		return location;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void parseName()
	{
		String realName;
		String[] actualName = name.split("_");
		realName = actualName[0];
		
		for(int i = 1; i < actualName.length; i++)
		{
			realName += " "+actualName[i]; 
		}
		name = realName.substring(0, realName.length());
		
	}
	
	
	
	//public static final GeoPoint landmark1 = new GeoPoint(4.5979799, -74.0760842);
	
	public boolean drawLandmark(boolean state, MapView map)
	{
		parseName();
		Drawable nodeIcon = map.getResources().getDrawable(R.drawable.marker_node);	
		Marker startMarker = new Marker(map);
		startMarker.setPosition(location);
		if(state)
		{
			/**btn = (Button)(activity.findViewById(R.id.bubble_moreinfo));
			btn.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View view) {
		                Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
		        }
			});**/
			startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
			startMarker.setIcon(nodeIcon);
			startMarker.setSnippet(category);
			startMarker.setSubDescription(name);
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
	
	
	public class CustomInfoWindow extends MarkerInfoWindow {
        public CustomInfoWindow(MapView mapView) {
                super(R.layout.bonuspack_bubble, mapView);
        }
}
	
	

}

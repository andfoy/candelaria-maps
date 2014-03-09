package com.example.android.navigationdrawerexample;

import java.util.ArrayList;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

public class Route {
	
	private ArrayList<GeoPoint> routePoints;
	private RoadManager routeManager = new OSRMRoadManager();
	
	public Route(Landmarks[] landmarks, ArrayList<Integer> ids)
	{
		routePoints = new ArrayList<GeoPoint>();
		for(int i = 0; i < ids.size(); i++)
		{
			boolean pointAppended = false;
			for(int j = 0; j < landmarks.length && !pointAppended; j++)
			{
				if(landmarks[j].getId() == ids.get(i))
				{
					routePoints.add(landmarks[j].getLocation());
					pointAppended = true;
				}
			}
		}
	}
	
	public Road buildRoute()
	{
		Road route = routeManager.getRoad(routePoints);
		return route;
	}
	
	
	

}

/*
 * Copyright 2013 Edgar Andrés Margffoy Tuay 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

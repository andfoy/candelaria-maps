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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//import com.espian.showcaseview.ShowcaseView;
//import com.espian.showcaseview.targets.ViewTarget;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    
    private static boolean landmarksActive = true;
	private static MapView map;
	private static Landmarks[] landMarks;
	private static ArrayList<ArrayList<Integer>> ids = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<Integer> currentTour;
	static LocationFileReader reader = new LocationFileReader("https://raw.github.com/andfoy/candelaria-maps/master/test");
	static LocationFileReader tours = new LocationFileReader("https://raw.github.com/andfoy/candelaria-maps/master/tour");
	private Polyline routeOverlay;
	private static Route routeDraw; 
	
	private static double north = 4.615205;
    private static double east  = -74.065685;
    private static double south = 4.591816;
    private static double west  =  -74.084587;
    static double latitude = 0;
	static double longitude = 0;
	static double initLat = 4.5979799;
	static double initLong = -74.0760842;
	static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        StrictMode.ThreadPolicy policy = new
        		StrictMode.ThreadPolicy.Builder()
        				.permitAll().build();
        				StrictMode.setThreadPolicy(policy);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Button btn = (Button)(findViewById(R.id.bubble_moreinfo));

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
            mDrawerLayout.openDrawer(Gravity.LEFT);
            //View showcasedView = findViewById(R.id.drawer_layout);
            //ViewTarget target = new ViewTarget(showcasedView);
            //ShowcaseView.insertShowcaseView(target, this, R.string.showcase_title, R.string.showcase_details);
            
        }
      
        				
        //map = (MapView) findViewById(R.id.map);
        				
        //map.setTileSource(TileSourceFactory.MAPNIK);
        //map.setBuiltInZoomControls(true);
        //map.setMultiTouchControls(true);
        		      
        
        		      
        GPSTrack gps = new GPSTrack(MainActivity.this);
        //double latitude = 0, longitude = 0;
        				
        if(gps.canGetLocation())
        {
        	latitude = gps.getLatitude();
        	longitude = gps.getLongitude();
        }
        else
        {
        	gps.showSettingsAlert();
        }
        
        ids = readRoute();
        act = this;
        				
     	//BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);
        //map.setScrollableAreaLimit(bBox);

        //GeoPoint startPoint = new GeoPoint(latitude, longitude);
        //IMapController mapController = map.getController();
        //mapController.setZoom(16);
        //mapController.setCenter(startPoint);
        		        
        //Marker startMarker = new Marker(map);
        //startMarker.setPosition(startPoint);
        //startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        //map.getOverlays().add(startMarker);

        //map.invalidate();
        //drawLandmarks();
        
    }
    
    public static void drawLandmarks()
	{
        if(landmarksActive)
        {
        	int i;
        	if(landMarks == null)
        	{
        		try {
        			List<String[]> landmarks = reader.getFile();
        			landMarks = new Landmarks[landmarks.size()];
        			for(i = 0; i < landmarks.size(); i++)
        			{
        				String[] currentLandmark = landmarks.get(i);
        				GeoPoint locationPoint = new GeoPoint((Double.parseDouble(currentLandmark[0])), Double.parseDouble(currentLandmark[1]));
        				//makeToast("Loading Landmark: "+currentLandmark[3]+" Lat: "+currentLandmark[0]+" Long: "+currentLandmark[1]);
        				landMarks[i] = new Landmarks(act, locationPoint, currentLandmark[3], Integer.parseInt(currentLandmark[2]), currentLandmark[4]); 
        				boolean cond = landMarks[i].drawLandmark(landmarksActive, map);
        				//makeToast("Landmark draw: "+cond);
        			}
        		} catch (ClientProtocolException e) {
        		e.printStackTrace();
        		} catch (IOException e) {
        		e.printStackTrace();
        		}
        	}
        	else
        	{
        		for(i = 0; i < landMarks.length; i++)
    			{
    				boolean cond = landMarks[i].drawLandmark(landmarksActive, map);
    				//makeToast("Landmark draw: "+cond);
    			}
        	}
        }
        else
        {
        	map.getTileProvider().clearTileCache();
        	map.getOverlays().clear();
        }
	}
    
    public static ArrayList<ArrayList<Integer>> readRoute()
    {
    	ArrayList<ArrayList<Integer>> ids = new ArrayList<ArrayList<Integer>>();
        List<String[]> routes;
			try {
				routes = tours.getFile();
				for(int i = 0; i < routes.size(); i++)
	    		{
	    			String[] currentTour = routes.get(i);
	    			ArrayList<Integer> landmarksTour = new ArrayList<Integer>();
	    			for(int j = 0; j < currentTour.length; j++)
	    			{
	    				if(currentTour[j].matches("-?\\d+"))
	    				{
	    					landmarksTour.add(Integer.parseInt(currentTour[j]));
	    				}
	    			}
	    			ids.add(landmarksTour);
	    		}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	return ids;
    		
    }
    
    public void makeToast(String message)
	{
		Context context = getApplicationContext();
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, message, duration);
    	toast.show();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    		startActivity(intent);
	            return true;
	        case R.id.display_landmarks:
	        	landmarksActive = !landmarksActive;
	        	drawLandmarks();
                makeToast("Status is: "+landmarksActive);
	        	return true;
	        case R.id.about:
	        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	        	alertDialog.setTitle("Acerca de");
	        	alertDialog.setMessage("Candelaria Maps: Localizador de puntos de Interés en la ciudad de Bogotá \n Ver: 1.0.0");
	        	alertDialog.setButton("Volver", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        	// here you can add functions
	        	}
	        	});
	        	alertDialog.setIcon(R.drawable.ic_drawer);
	        	alertDialog.show();
	        default:
	            return super.onOptionsItemSelected(item);    
	    }
	}

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            
            //makeToast(""+position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
    	if(position == 0)
    	{
    		Fragment fragment = new PlanetFragment();
    		makeToast(""+position);
    		Bundle args = new Bundle();
    		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
    		fragment.setArguments(args);

    		FragmentManager fragmentManager = getFragmentManager();
    		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    	}
    	else
    	{
    		map.getOverlays().clear();
    		
    		GeoPoint startPoint = new GeoPoint(latitude, longitude);
    		
    		Marker startMarker = new Marker(map);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
            map.getOverlays().add(startMarker);
            
            drawLandmarks();
    		
    		routeDraw = new Route(landMarks, ids.get(position-1));
    		Road routePoints = routeDraw.buildRoute();
    		routeOverlay = RoadManager.buildRoadOverlay(routePoints, this);
    		map.getOverlays().add(routeOverlay);
    		map.invalidate();
    	}

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "drawable", getActivity().getPackageName());
            //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            map = (MapView) rootView.findViewById(R.id.map);
			
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);
            
            double north = 4.615205;
            double east  = -74.065689;
            double south = 4.591816;
            double west  =  -74.084587;
            		    
            				
         	BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);
            map.setScrollableAreaLimit(bBox);

            GeoPoint startPoint = new GeoPoint(latitude, longitude);
            GeoPoint initPoint = new GeoPoint(initLat, initLong);
            IMapController mapController = map.getController();
            mapController.setZoom(16);
            mapController.setCenter(initPoint);
            		        
            Marker startMarker = new Marker(map);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
            map.getOverlays().add(startMarker);

            map.invalidate();
            drawLandmarks();
            getActivity().setTitle(planet);
            return rootView;
        }
    }
}
package com.parse.anywall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.LatLngBounds;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnLoadedListener;
import com.tencent.tencentmap.mapsdk.map.OnMapCameraChangeListener;
import com.tencent.tencentmap.mapsdk.map.OnMarkerPressListener;

public class MainActivityWhm extends FragmentActivity {
	
	// Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 200;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;
    
    private static final int SEARCH_RADIUS = 100;  // 100 KILOMETERS
	  
	  
    // uis
	private MapView mMapView;
	private Button mBtnPost;
	private TextView mCurrentLocationName;
	
	//internal state
	private LatLng  mCurrentLocaiotn;
	private LatLng  mLastLocation = new LatLng(39.907937 , 116.398647);
	private Marker  mCurrentLocationMarker;
	
	private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
	
	
	// tentent location sdk
	private TencentLocationListener mLocationListener = new TencentLocationListener(){

		@Override
		public void onLocationChanged(TencentLocation location	, int error,
				String reason) {
			// TODO Auto-generated method stub
			if(TencentLocation.ERROR_OK == error){
				mLastLocation = mCurrentLocaiotn ;
				mCurrentLocaiotn = new LatLng(location.getLatitude(),location.getLongitude());
				
				if(mCurrentLocationMarker == null){
					
					mCurrentLocationMarker = mMapView.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_location))
					.position(mCurrentLocaiotn));
					
					// only for first location, animate to current locaiton
					GeoPoint currentGeoPoint = new GeoPoint((int)(mCurrentLocaiotn.getLatitude()*1E6),(int)(mCurrentLocaiotn.getLongitude()*1E6));
					mMapView.getController().animateTo(currentGeoPoint);
				}
				else {
					mCurrentLocationMarker.setPosition(mCurrentLocaiotn);
				}
				
				// display current location in ui
				mCurrentLocationName.setText("当前位置 : "+location.getName());
		
			}
			else {
				
				Toast.makeText(MainActivityWhm.this, "location error:"+reason, Toast.LENGTH_LONG);
				if(mCurrentLocationMarker != null){
					mCurrentLocationMarker.remove();
				}
			}
		}

		@Override
		public void onStatusUpdate(String arg0, int arg1, String arg2) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_main_whm);
		
		mMapView = (MapView)findViewById(R.id.mapview); 
		mMapView.onCreate(arg0);
		initMapView(mMapView);
		
		mCurrentLocationName = (TextView)findViewById(R.id.current_location);
		mCurrentLocationName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mCurrentLocaiotn != null){
					GeoPoint currentGeoPoint = new GeoPoint((int)(mCurrentLocaiotn.getLatitude()*1E6),(int)(mCurrentLocaiotn.getLongitude()*1E6));
					mMapView.getController().animateTo(currentGeoPoint);
				}
			}
		});
		
		
		mBtnPost = (Button)findViewById(R.id.post_button);
		mBtnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		
				LatLng myLoc = (mCurrentLocaiotn == null)? mLastLocation : mCurrentLocaiotn ;
				  if(myLoc == null){
					  Toast.makeText(MainActivityWhm.this,
					            "Please try again after your location appears on the map.", Toast.LENGTH_LONG).show();
					  return; 
				  }
				
			      Intent intent = new Intent(MainActivityWhm.this, PostActivity.class);
			      Bundle bundle = new Bundle();  
			      bundle.putDouble("longitude", myLoc.getLongitude());
			      bundle.putDouble("latitude", myLoc.getLatitude()); 
			      intent.putExtra(Application.INTENT_EXTRA_LOCATION, bundle);
			      startActivity(intent);
			}
		});
	}
	
	
	private void initMapView(MapView mapView){
		
		mapView.getController().setZoom(13);
		mapView.getController().setOnMapLoadedListener(new OnLoadedListener() {
			
			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				doMapQuery_whm();
			}
		});
		
		mapView.getController().setOnMapCameraChangeListener(new OnMapCameraChangeListener() {
			
			@Override
			public void onCameraChangeFinish(CameraPosition arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub
				doMapQuery_whm();
			}
		});
		
		mapView.getController().setOnMarkerClickListener(new OnMarkerPressListener() {
			
			@Override
			public void onMarkerPressed(Marker marker) {
				// TODO Auto-generated method stub
				marker.showInfoWindow();
			}
		});
		
		
	}
	
	private void doMapQuery_whm(){
		LatLng mapCenter = mMapView.getMapCenter();
		
		// get span
		final ParseGeoPoint screenSouthWest = new ParseGeoPoint(mapCenter.getLatitude()-(mMapView.getLatitudeSpan()/(2*1E6)),
			                                              mapCenter.getLongitude()-(mMapView.getLatitudeSpan()/(2*1E6))	);
		final ParseGeoPoint screenNorthEast = new ParseGeoPoint(mapCenter.getLatitude()+(mMapView.getLatitudeSpan()/(2*1E6)),
                mapCenter.getLongitude()+(mMapView.getLatitudeSpan()/(2*1E6))	);
		
		// do the query
		ParseQuery<AnywallPost> mapQuery = AnywallPost.getQuery();
		mapQuery.include("user");
		mapQuery.orderByDescending("createAt");
		mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);
		mapQuery.findInBackground(new FindCallback<AnywallPost>() {

			@Override
			public void done(List<AnywallPost> objects, ParseException e) {
				// TODO Auto-generated method stub
				if(e != null){
					Toast.makeText(MainActivityWhm.this,"数据查询错误："+e.getMessage(),Toast.LENGTH_LONG).show();
					return;
				}
				else {
					// clear all the markers which are not in the scope of view
					for(String objectId : new HashSet<String>(mapMarkers.keySet())){
						Marker marker = mapMarkers.get(objectId);
						
						LatLngBounds bounds = new LatLngBounds(new LatLng(screenSouthWest.getLatitude(),screenSouthWest.getLongitude()),
															   new LatLng(screenNorthEast.getLatitude(),screenNorthEast.getLongitude())	);
						if(!bounds.contains(marker.getPosition())){
							marker.remove();
							mapMarkers.remove(objectId);
						}
					}
					// add all newly added markers
					for(AnywallPost post : objects){
						if(!mapMarkers.containsKey(post.getObjectId())){
							MarkerOptions options = new MarkerOptions()
								.title(post.getText())
								.snippet(post.getUser().getUsername())
								.icon(BitmapDescriptorFactory.defaultMarker())
								.position(new LatLng(post.getLocation().getLatitude(),post.getLocation().getLongitude()));
							
							Marker marker = mMapView.addMarker(options);
							mapMarkers.put(post.getObjectId(), marker);
						}
					}
				}
			
			}
		});
	}
	 /*
	   * Set up the query to update the map view
	   */
	  private void doMapQuery() {
	   
		 LatLng myLoc = (mCurrentLocaiotn == null)? mLastLocation : mCurrentLocaiotn ;
		  
	    // If location info isn't available, clean up any existing markers
	    if (myLoc == null) {
	      cleanUpMarkers(new HashSet<String>());
	      return;
	    }
	    final ParseGeoPoint myPoint = new ParseGeoPoint(myLoc.getLatitude(),myLoc.getLongitude());
	    // Create the map Parse query
	    ParseQuery<AnywallPost> mapQuery = AnywallPost.getQuery();
	    // Set up additional query filters
	    mapQuery.whereWithinKilometers("location", myPoint, MAX_POST_SEARCH_DISTANCE);
	    mapQuery.include("user");
	    mapQuery.orderByDescending("createdAt");
	    mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);
	    // Kick off the query in the background
	    mapQuery.findInBackground(new FindCallback<AnywallPost>() {
	      @Override
	      public void done(List<AnywallPost> objects, ParseException e) {
	        if (e != null) {
	          if (Application.APPDEBUG) {
	            Log.d(Application.APPTAG, "An error occurred while querying for map posts.", e);
	          }
	          return;
	        }
	      
	        // Posts to show on the map
	        Set<String> toKeep = new HashSet<String>();
	        // Loop through the results of the search
	        for (AnywallPost post : objects) {
	          // Add this post to the list of map pins to keep
	          toKeep.add(post.getObjectId());
	          // Check for an existing marker for this post
	          Marker oldMarker = mapMarkers.get(post.getObjectId());
	          // Set up the map marker's location
	          MarkerOptions markerOpts =
	              new MarkerOptions().position(new LatLng(post.getLocation().getLatitude(), post
	                  .getLocation().getLongitude()));
	          // Set up the marker properties based on if it is within the search radius
	          if (post.getLocation().distanceInKilometersTo(myPoint) > SEARCH_RADIUS) {
	            // Check for an existing out of range marker
	            if (oldMarker != null) {
	              if (oldMarker.getSnippet() == null) {
	                // Out of range marker already exists, skip adding it
	                continue;
	              } else {
	                // Marker now out of range, needs to be refreshed
	                oldMarker.remove();
	              }
	            }
	            // Display a red marker with a predefined title and no snippet
	            markerOpts =
	                markerOpts.title(getResources().getString(R.string.post_out_of_range)).icon(
	                    BitmapDescriptorFactory.defaultMarker());
	          } else {
	            // Check for an existing in range marker
	            if (oldMarker != null) {
	              if (oldMarker.getSnippet() != null) {
	                // In range marker already exists, skip adding it
	                continue;
	              } else {
	                // Marker now in range, needs to be refreshed
	                oldMarker.remove();
	              }
	            }
	            // Display a green marker with the post information
	            markerOpts =
	                markerOpts.title(post.getText()).snippet(post.getUser().getUsername())
	                    .icon(BitmapDescriptorFactory.defaultMarker());
	          }
	          // Add a new marker
	          Marker marker = mMapView.addMarker(markerOpts);
	          mapMarkers.put(post.getObjectId(), marker);
	          
	          
//	          if (post.getObjectId().equals(selectedPostObjectId)) {
//	            marker.showInfoWindow();
//	            selectedPostObjectId = null;
//	          }
	        }
	        // Clean up old markers.
	        cleanUpMarkers(toKeep);
	      }
	    });
	  }

	  /*
	   * Helper method to clean up old markers
	   */
	  private void cleanUpMarkers(Set<String> markersToKeep) {
	    for (String objId : new HashSet<String>(mapMarkers.keySet())) {
	      if (!markersToKeep.contains(objId)) {
	        Marker marker = mapMarkers.get(objId);
	        marker.remove();
	        mapMarkers.get(objId).remove();
	        mapMarkers.remove(objId);
	      }
	    }
	  }
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.onDestroy();
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();
		
		//register location request
		TencentLocationRequest request = TencentLocationRequest.create();
		request.setInterval(0);
		
		if(TencentLocationManager.getInstance(this).requestLocationUpdates(request, mLocationListener) != 0){
			Toast.makeText(this, "request location updates failed", Toast.LENGTH_LONG).show();
		}
		
		doMapQuery_whm();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		super.onPause();
		
		TencentLocationManager.getInstance(this).removeUpdates(mLocationListener);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mMapView.onStop();
		super.onStop();
	}
}

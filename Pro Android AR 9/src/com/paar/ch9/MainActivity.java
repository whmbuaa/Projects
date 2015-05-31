package com.paar.ch9;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.R.color;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

public class MainActivity extends AugmentedActivity {
    private static final String TAG = "MainActivity";
    private static final String locale = "en";
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);
	private static final Map<String,NetworkDataSource> sources = new ConcurrentHashMap<String,NetworkDataSource>();    

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     
        
        LocalDataSource localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers());

        NetworkDataSource twitter = new TwitterDataSource(this.getResources());
        sources.put("twitter",twitter);
        NetworkDataSource wikipedia = new WikipediaDataSource(this.getResources());
        sources.put("wiki",wikipedia);
        
        
    }

	@Override
    public void onStart() {
        super.onStart();
        
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected() item="+item);
        switch (item.getItemId()) {
            case R.id.showRadar:
                showRadar = !showRadar;
                item.setTitle(((showRadar)? "Hide" : "Show")+" Radar");
                break;
            case R.id.showZoomBar:
                showZoomBar = !showZoomBar;
                item.setTitle(((showZoomBar)? "Hide" : "Show")+" Zoom Bar");
                zoomLayout.setVisibility((showZoomBar)?LinearLayout.VISIBLE:LinearLayout.GONE);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

	@Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        
        updateData(location.getLatitude(),location.getLongitude(),location.getAltitude());
    }

	@Override
	protected void markerTouched(Marker marker) {
        Toast t = Toast.makeText(getApplicationContext(), marker.getName(), Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
	}

    @Override
	protected void updateDataOnZoom() {
	    super.updateDataOnZoom();
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
	}
    
    private void updateData(final double lat, final double lon, final double alt) {
    	Log.i("whm","updateData--------called.");
//        try {
//            exeService.execute(
//                new Runnable() {
//                    
//                    public void run() {
//                        for (NetworkDataSource source : sources.values())
//                            download(source, lat, lon, alt);
//                    }
//                }
//            );
//        } catch (RejectedExecutionException rej) {
//            Log.w(TAG, "Not running new download Runnable, queue is full.");
//        } catch (Exception e) {
//            Log.e(TAG, "Exception running download Runnable.",e);
//        }
    	getPoiMarkersFromBaiduMap(lat,lon,alt);
    }
    
    private static boolean download(NetworkDataSource source, double lat, double lon, double alt) {
		if (source==null) return false;
		
		String url = null;
		try {
			url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);    	
		} catch (NullPointerException e) {
			return false;
		}
    	
		List<Marker> markers = null;
		try {
			markers = source.parse(url);
		} catch (NullPointerException e) {
			return false;
		}

    	ARData.addMarkers(markers);
    	return true;
    }
    
    
    
    private  boolean getPoiMarkersFromBaiduMap(double lat, double lon, double alt) {
    	
    	Log.i("whm","getPoiMarkersFromBaiduMap-----called.");
    	PoiSearch poiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult poiResult) {
				
				Log.i("whm","onGetPoiResult---called.");
				// TODO Auto-generated method stub
				if(poiResult.error == PoiResult.ERRORNO.NO_ERROR){
					List<PoiInfo> poiList = poiResult.getAllPoi();
					Log.i("whm","find poiInfo number: "+poiList.size());
					List<Marker> markers = new LinkedList<Marker>();
					for(PoiInfo poiInfo : poiList){
						Log.i("whm","find poiInfo: "+poiInfo.toString());
						Marker marker = new IconMarker(poiInfo.name, poiInfo.location.latitude, poiInfo.location.longitude, 0, color.darker_gray,BitmapFactory.decodeResource(getResources(), R.drawable.poi_balloon));
						markers.add(marker);
					}
					
					ARData.addMarkers(markers);
				}
				else{
//					Toast.makeText(MainActivity.this, "查找兴趣点失败"+poiResult.error.toString(), Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
				// TODO Auto-generated method stub
				
			}
		};
		
		poiSearch.setOnGetPoiSearchResultListener(poiListener);
		poiSearch.searchNearby(new PoiNearbySearchOption()
				.location(new LatLng(lat,lon))
				.keyword("酒店")
				.pageCapacity(10)
				.pageNum(0)
				.radius((int)ARData.getRadius()*1000)
				.sortType(PoiSortType.comprehensive));
    	
    	return true;
    }
    
    
}
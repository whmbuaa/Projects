package com.tencent.mapsdksample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tencentmapsdksample.R;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnInforWindowClickListener;
import com.tencent.tencentmap.mapsdk.map.OnMapLongPressListener;
import com.tencent.tencentmap.mapsdk.map.OnMarkerDragedListener;
import com.tencent.tencentmap.mapsdk.map.OnMarkerPressListener;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;

public class MarkerActivity extends MapActivity implements OnMarkerDragedListener,
OnMarkerPressListener, OnInforWindowClickListener {
	private MapView mapView;
	private MapController mapController;
	private Marker markerFix;
	private Marker markerLongPress;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_marker);
		init();
	}
	
	private void init() {
		mapView = (MapView)findViewById(R.id.map);
		//mapView.onCreate(arg0);
		mapView.getController().setZoom(11);

		mapController = mapView.getController();
		//标注点击监听
		mapController.setOnMarkerClickListener(this);
		//标注拖动监听
		mapController.setOnMarkerDragListener(this);
		//InfoWindow点击监听
		mapController.setOnInforWindowClickListener(this);
		//地图长按监听
		mapController.setOnMapPressClickLisener(mapLongPressListener);
		addMarkers(mapView);
		Toast toast = Toast.makeText(this, "长按地图添加Marker", Toast.LENGTH_LONG);
		toast.show();
	}
	
	OnMapLongPressListener mapLongPressListener = new OnMapLongPressListener() {

		@Override
		public void onMapLongPress(LatLng arg0) {
			// TODO Auto-generated method stub
			if (markerLongPress == null) {
				markerLongPress = mapView.addMarker(
						new MarkerOptions()
						.draggable(true)
						.icon(BitmapDescriptorFactory
								.defaultMarker())
						.position(arg0)
						.title("longPressMarker")
						.snippet(arg0.toString()));
			} else {
				markerLongPress.setPosition(arg0);
				markerLongPress.setSnippet(arg0.toString());
			}
			
			markerLongPress.showInfoWindow();
			//添加完标注需要刷新地图才能显示
			mapView.refreshMap();
		}
		
	};

	/**
	 * 通过添加OverlayItem添加标注物
	 * @param mapView
	 */
	private void addMarkers(MapView mapView) {
		GeoPoint p1 = new GeoPoint((int)(39.90403 * 1E6), (int)(116.407525 * 1E6));
		GeoPoint p2 = new GeoPoint((int)(38.5 * 1E6), (int)(114.955 * 1E6));
		LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
		Drawable marker = getResources().getDrawable(R.drawable.route_start);

		OverlayItem oiFixed = new OverlayItem(p1, "标注1", "不可拖拽");
		oiFixed.setDragable(false);
		
		OverlayItem oiDrag = new OverlayItem(p2, "标注2", "可拖拽,自定义图标", marker);

		mapView.add(oiFixed);		
		mapView.add(oiDrag);
		
		markerFix = mapView.addMarker(new MarkerOptions()
		.position(SHANGHAI)
		.title("上海")
		.anchor(0.5f, 0.5f)
		.icon(BitmapDescriptorFactory
				.defaultMarker())
				.draggable(true));
		addHundredMarkers(mapView);
		markerFix.showInfoWindow();// 设置默认显示一个infowinfow
		
		//BitmapDescriptorFactory相关方法使用
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 100))
		.icon(BitmapDescriptorFactory.fromAsset("green_location.ico"))
		.title("from asset"));

		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 102.0))
		.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.route_start)))
		.title("from bitmap"));
		
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 104.0))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_location))
		.title("from resource"));
		
		File file = new File(getFilesDir(), "myicon.ico");
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromAsset("green_location.ico");
		Bitmap bmp = bitmapDescriptor.getBitmap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		FileOutputStream fos;
		try {
			fos = openFileOutput(file.getName(), MODE_PRIVATE);
			try {
				fos.write(baos.toByteArray());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 106.0))
		.icon(BitmapDescriptorFactory.fromPath(getFilesDir() + "/" + file.getName()))
		.title("from path"));
		
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 108.0))
		.icon(BitmapDescriptorFactory.fromFile(file.getName()))
		.title("from file"));
	}

	private void addHundredMarkers(MapView mapView) {
		double lat = 20.5;
		double lng = 90.955;
		LatLng latLng = new LatLng(lat, lng);
		int i = 0;
		while (i < 100) {
			mapView.addMarker(new MarkerOptions()
			.position(latLng)
			.title("marker" + i++)
			.icon(BitmapDescriptorFactory.
					defaultMarker())
					.draggable(false));
			latLng = new LatLng(lat += 0.5, lng += 0.5);
			Log.e("draw", "marker:" + i);
		}
	}
	
	@Override
	public void onMarkerPressed(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.showInfoWindow();
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.setSnippet(arg0.getPosition().toString());
		arg0.showInfoWindow();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInforWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.setSnippet("InfoWindow Clicked!");
		arg0.showInfoWindow();
	}
	
}

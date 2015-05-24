package com.tencent.mapsdksample;

import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.mapsdk.raster.model.Polygon;
import com.tencent.mapsdk.raster.model.PolygonOptions;
import com.tencent.mapsdk.raster.model.Polyline;
import com.tencent.mapsdk.raster.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapFragmentUtil;
import com.tencent.tencentmap.mapsdk.map.OnLoadedListener;
import com.tencent.tencentmap.mapsdk.map.OnMapCameraChangeListener;
import com.tencent.tencentmap.mapsdk.map.OnMapHitListener;
import com.tencent.tencentmap.mapsdk.map.OnMapLongPressListener;
import com.tencent.tencentmap.mapsdk.map.OnMarkerDragedListener;
import com.tencent.tencentmap.mapsdk.map.QSupportMapFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapFragmentActivity extends FragmentActivity{
	private QSupportMapFragment qMapFragment;
	private MapFragmentUtil mapFragUtil;
	private MapController mapController;
	private TextView tvMonitor;
	private Button btnAnimate;
	private Button btnMarker;
	private Button btnGeometry;
	private LinearLayout mainLayout;
	private FrameLayout mapFrame;
	
	private GeoPoint mGeoPoint;
	private LatLng mLatLng;
	private Marker mMarker;
	private Polyline mPolyline;
	private Polygon mpPolygon;
	private Circle mCircle;
	int id = 0x7f071001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundColor(0xffffffff);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(mainLayout);
		init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mapFragUtil == null) {
			mapFragUtil = new MapFragmentUtil(qMapFragment);
			mapController = mapFragUtil.getMapController();
		}
		bindLinstener();
		
		Log.e("get zoom level", Integer.toString(mapFragUtil.getZoomLevel()));
	}
	
	private void init() {
		LinearLayout lineOne = new LinearLayout(this);
		lineOne.setOrientation(LinearLayout.HORIZONTAL);
		mainLayout.addView(lineOne);
		
		btnAnimate = new Button(this);
		btnAnimate.setText("移动到中关村");
		lineOne.addView(btnAnimate);
		
		btnMarker = new Button(this);
		btnMarker.setText("添加Marker");
		lineOne.addView(btnMarker);
		
		btnGeometry = new Button(this);
		btnGeometry.setText("添加图形");
		lineOne.addView(btnGeometry);
		
		tvMonitor = new TextView(this);
		tvMonitor.setTextColor(0xff000000);
		tvMonitor.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mainLayout.addView(tvMonitor);
		
		if (qMapFragment != null) {
			return;
		}
		qMapFragment = QSupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		mapFrame = new FrameLayout(this);
		
		mapFrame.setId(id);
		fragmentTransaction.add(id, qMapFragment);
		fragmentTransaction.commit();
		mainLayout.addView(mapFrame);
	}
	
	private void bindLinstener() {
		final LatLng latLng1 = new LatLng(39.925961,116.388171);
		final LatLng latLng2 = new LatLng(39.735961,116.488171);
		final LatLng latLng3 = new LatLng(39.635961,116.268171);

		final PolylineOptions lineOpt = new PolylineOptions();
		lineOpt.add(latLng1);
		lineOpt.add(latLng2);
		lineOpt.add(latLng3);
		
		final LatLng latLng4 = new LatLng(39.935961,116.388171);
		final LatLng latLng5 = new LatLng(40.035961,116.488171);
		final LatLng latLng6 = new LatLng(40.095961,116.498171);
		final LatLng latLng7 = new LatLng(40.135961,116.478171);
		final LatLng latLng8 = new LatLng(40.095961,116.398171);
		final PolygonOptions polygonOp = new PolygonOptions();
		polygonOp.fillColor(0x55000077);
		polygonOp.strokeWidth(4);
		polygonOp.add(latLng4);
		polygonOp.add(latLng5);
		polygonOp.add(latLng6);
		polygonOp.add(latLng7);
		polygonOp.add(latLng8);
		
		final LatLng latLng9 = new LatLng(39.735961,116.788171);
		final CircleOptions circleOp = new CircleOptions();
		circleOp.center(latLng9);
		circleOp.radius(5000);
		circleOp.strokeColor(0xff0000ff);
		circleOp.strokeWidth(5);
		circleOp.fillColor(0xff00ff00);
		mGeoPoint = new GeoPoint((int)(39.980484 * 1e6), (int)(116.311302 * 1e6));//中关村
		
		btnAnimate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnAnimate.getText().toString().equals("移动到中关村")) {
					mapController.animateTo(mGeoPoint);
					btnAnimate.setText("停止动画");
				} else {
					mapController.stopAnimte();
				}
			}
		});
		
		btnMarker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnMarker.getText().toString().equals("添加Marker")) {
					mLatLng = new LatLng(39.984122, 116.307484);
					mMarker = mapFragUtil.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.defaultMarker())
					.position(mLatLng)
					.draggable(true));
					
					btnMarker.setText("删除Marker");
					mapFragUtil.refreshMap();
				} else {
					mapFragUtil.removeOverlay(mMarker);
					btnMarker.setText("添加Marker");
				}
				
			}
		});
		
		btnGeometry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnGeometry.getText().toString().equals("添加图形")) {
					mpPolygon = mapFragUtil.addPolygon(polygonOp);
					mPolyline = mapFragUtil.addPolyline(lineOpt);
					mCircle = mapFragUtil.addCircle(circleOp);
					btnGeometry.setText("删除图形");
				} else {
					mapFragUtil.removeOverlay(mpPolygon);
					mapFragUtil.removeOverlay(mPolyline);
					mapFragUtil.removeOverlay(mCircle);
					btnGeometry.setText("添加图形");
				}
				
			}
		});
		mapController.setOnMapLoadedListener(new OnLoadedListener() {
			
			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				Log.e("Listener", "Map Loaded");
			}
		});
		mapController.setOnMapCameraChangeListener(new OnMapCameraChangeListener() {
			
			@Override
			public void onCameraChangeFinish(CameraPosition arg0) {
				// TODO Auto-generated method stub
				tvMonitor.setText( "Camera Change Finish:" + 
						"Target:" + arg0.getTarget().toString() + 
						"zoom level:" + arg0.getZoom());
				btnAnimate.setText("移动到中关村");
			}
			
			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub
				Log.e("Listener", "Camera Change");
			}
		});
		mapController.setOnMapHitListener(new OnMapHitListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Log.e("Listener", "Map Click");
			}
		});
		mapController.setOnMapPressClickLisener(new OnMapLongPressListener() {
			
			@Override
			public void onMapLongPress(LatLng arg0) {
				// TODO Auto-generated method stub
				Log.e("Listener", "Map Long Press");
			}
		});
		
		mapController.setOnMarkerDragListener(new OnMarkerDragedListener() {

			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
				tvMonitor.setText("Marker Dragging");
			}

			@Override
			public void onMarkerDragEnd(Marker arg0) {
				// TODO Auto-generated method stub
				tvMonitor.setText("Marker Drag End");
			}

			@Override
			public void onMarkerDragStart(Marker arg0) {
				// TODO Auto-generated method stub
				tvMonitor.setText("Marker Drag Start");
			}
		});
	}
}

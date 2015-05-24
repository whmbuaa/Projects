package com.tencent.mapsdksample;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tencentmapsdksample.R;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Polygon;
import com.tencent.mapsdk.raster.model.PolygonOptions;
import com.tencent.mapsdk.raster.model.Polyline;
import com.tencent.mapsdk.raster.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnScreenShotListener;

public class MapOverlayActivity extends MapActivity {

	private MapView mapView;
	private Button btnPolyline;
	private Button btnPolygon;
	private Button btnCircle;
	private Button btnRemove;
	private Button btnScreenShot;
	private Spinner spinDeleteOverlay;
	private ArrayAdapter<String> arrAdapOverlay;
	private List<Object> listOverlay;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_map_overlay);
		init();
	}
	
	private void init() {
		mapView = (MapView)findViewById(R.id.map);

		btnPolyline = (Button)findViewById(R.id.btn_polyline);
		btnPolygon = (Button)findViewById(R.id.btn_polygon);
		btnCircle = (Button)findViewById(R.id.btn_circle);
		btnRemove = (Button)findViewById(R.id.btn_remove);
		btnScreenShot = (Button)findViewById(R.id.btn_screen_shot);
		spinDeleteOverlay = (Spinner)findViewById(R.id.spin_delete);
		arrAdapOverlay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		arrAdapOverlay.add("删除选择的Overlay");
		
		listOverlay = new ArrayList<Object>();
		spinDeleteOverlay.setAdapter(arrAdapOverlay);

		btnPolyline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listOverlay.add(drawPolyline());
				arrAdapOverlay.add("删除多段线");
			}
		});

		btnPolygon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listOverlay.add(drawPolygon());
				arrAdapOverlay.add("删除多边形");
			}
		});

		btnCircle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listOverlay.add(drawCircle());
				arrAdapOverlay.add("删除圆");
			}
		});

		spinDeleteOverlay.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d("selection", Integer.toString(position));
				if (position != 0) {
					mapView.removeOverlay(listOverlay.remove(position - 1));
					arrAdapOverlay.remove(arrAdapOverlay.getItem(position));
					spinDeleteOverlay.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		btnRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapView.clearAllOverlays();
				arrAdapOverlay.clear();
				arrAdapOverlay.add("删除选择的Overlay");
			}
		});
		
		btnScreenShot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapView.getController().getScreenShot(new OnScreenShotListener() {
					
					@Override
					public void onMapScreenShot(Bitmap arg0) {
						// TODO Auto-generated method stub
						
						Toast toast  = Toast.makeText(MapOverlayActivity.this, "截屏成功", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						ImageView imageView = new ImageView(MapOverlayActivity.this);
						imageView.setImageBitmap(arg0);
						LinearLayout toastImage = (LinearLayout)toast.getView();
						toastImage.addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT, 
								LayoutParams.WRAP_CONTENT));
						toast.show();
					}
				});
			}
		});
	}
	
	private Polyline drawPolyline() {
		final LatLng latLng1 = new LatLng(39.925961,116.388171);
		final LatLng latLng2 = new LatLng(39.735961,116.488171);
		final LatLng latLng3 = new LatLng(39.635961,116.268171);

		PolylineOptions lineOpt = new PolylineOptions();
		lineOpt.add(latLng1);
		lineOpt.add(latLng2);
		lineOpt.add(latLng3);

		Polyline line = mapView.addPolyline(lineOpt);
		//添加完后需要刷新地图
		mapView.refreshMap();
		return line;
	}
	private Polygon drawPolygon() {
		final LatLng latLng1 = new LatLng(39.935961,116.388171);
		final LatLng latLng2 = new LatLng(40.035961,116.488171);
		final LatLng latLng3 = new LatLng(40.095961,116.498171);
		final LatLng latLng4 = new LatLng(40.135961,116.478171);
		final LatLng latLng5 = new LatLng(40.095961,116.398171);
		PolygonOptions polygonOp = new PolygonOptions();
		polygonOp.fillColor(0x55000077);
		polygonOp.strokeWidth(4);
		polygonOp.add(latLng1);
		polygonOp.add(latLng2);
		polygonOp.add(latLng3);
		polygonOp.add(latLng4);
		polygonOp.add(latLng5);
		Polygon polygon = mapView.addPolygon(polygonOp);
		mapView.refreshMap();
		return polygon;
	}
	private Circle drawCircle() {
		final LatLng latLng1 = new LatLng(39.735961,116.788171);
		CircleOptions circleOp = new CircleOptions();
		circleOp.center(latLng1);
		circleOp.radius(5000);
		circleOp.strokeColor(0xff0000ff);
		circleOp.strokeWidth(5);
		circleOp.fillColor(0xff00ff00);
		Circle circle = mapView.addCircle(circleOp);

		mapView.refreshMap();
		return circle;
	}
}

package com.tencent.mapsdksample;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tencentmapsdksample.R;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnLoadedListener;
import com.tencent.tencentmap.mapsdk.map.OnMapCameraChangeListener;
import com.tencent.tencentmap.mapsdk.map.OnMapHitListener;
import com.tencent.tencentmap.mapsdk.map.OnMapLongPressListener;

public class MapControlActivity extends MapActivity implements 
OnMapCameraChangeListener, OnLoadedListener, OnMapHitListener, OnMapLongPressListener{
	MapView mapView;
	//CheckBox cbTraffic;
	CheckBox cbSatellite;
	CheckBox cbScale;
	EditText etZoomLevel;
	EditText etScalePosition;
	EditText etLogoPosition;
	LinearLayout linearLayout;
	LinearLayout llLog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_control_map);
		init();
	}

	private void init() {
		linearLayout = (LinearLayout)findViewById(R.id.ll_panel);
		mapView = (MapView)findViewById(R.id.map);
		cbSatellite = (CheckBox)findViewById(R.id.cb_satelite);
		cbScale = (CheckBox)findViewById(R.id.cb_scale);
		etZoomLevel = (EditText)findViewById(R.id.et_zoom);
		etScalePosition = (EditText)findViewById(R.id.et_set_scale_position);
		etLogoPosition = (EditText)findViewById(R.id.et_set_logo_position);
		llLog = (LinearLayout)findViewById(R.id.ll_showLog);
		final InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		final MapController mapController = mapView.getController();

		etZoomLevel.setHint(new String("缩放级别" + 
				mapView.getMinZoomLevel() + "-" + mapView.getMaxZoomLevel()));

		mapView.setSatellite(false);

		mapController.setOnMapCameraChangeListener(this);
		mapController.setOnMapLoadedListener(this);
		mapController.setOnMapHitListener(this);
		mapController.setOnMapPressClickLisener(this);

		//卫星图开关
		cbSatellite.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mapView.setSatellite(isChecked);
			}
		});
		//内置比例尺开关
		cbScale.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mapView.setScalControlsEnable(isChecked);
			}
		});

		linearLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				etZoomLevel.clearFocus();
				return false;
			}
		});

		etZoomLevel.setOnFocusChangeListener(new OnFocusChangeListener() {
			int zoomLevel = 0;

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					imm.hideSoftInputFromWindow(etZoomLevel.getWindowToken(), 0);
					try {
						zoomLevel = Integer.valueOf(etZoomLevel.getText().toString());
					} catch (NumberFormatException e) {
						// TODO: handle exception
						Toast toast = Toast.makeText(MapControlActivity.this, 
								"缩放级别应为" + mapView.getMinZoomLevel() + "~" + mapView.getMaxZoomLevel() + "的整数",
								Toast.LENGTH_SHORT);
						toast.show();

					}
					if (zoomLevel < mapView.getMinZoomLevel() || 
							zoomLevel > mapView.getMaxZoomLevel()) {
						Toast toast = Toast.makeText(MapControlActivity.this, 
								"缩放级别应为" + mapView.getMinZoomLevel() + "~" + mapView.getMaxZoomLevel() + "的整数",
								Toast.LENGTH_SHORT);
						toast.show();
						return;
					}
					mapController.setZoom(zoomLevel);
				}
			}
		});
		etScalePosition.setOnFocusChangeListener(new OnFocusChangeListener() {
			int positon = 0;
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					imm.hideSoftInputFromWindow(etScalePosition.getWindowToken(), 0);
					try {
						positon = Integer.valueOf(etScalePosition.getText().toString());
					} catch (NumberFormatException e) {
						// TODO: handle exception
						Toast toast = Toast.makeText(MapControlActivity.this, 
								"比例尺位置0~2的整数",
								Toast.LENGTH_SHORT);
						toast.show();
					}
					if (positon < 0 || positon >2) {
						Toast toast = Toast.makeText(MapControlActivity.this, 
								"比例尺位置0~2的整数",
								Toast.LENGTH_SHORT);
						toast.show();
						return;
					}
					mapView.setScaleViewPosition(positon);
				}
			}

		});
		etLogoPosition.setOnFocusChangeListener(new OnFocusChangeListener() {
			int positon = 0;
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					imm.hideSoftInputFromWindow(etLogoPosition.getWindowToken(), 0);
					try {
						positon = Integer.valueOf(etLogoPosition.getText().toString());
					} catch (NumberFormatException e) {
						// TODO: handle exception
						Toast toast = Toast.makeText(MapControlActivity.this, 
								"Logo位置为0~5的整数",
								Toast.LENGTH_SHORT);
						toast.show();

					}
					if (positon < 0 || positon >2) {
						Toast toast = Toast.makeText(MapControlActivity.this, 
								"Logo位置为0~5的整数",
								Toast.LENGTH_SHORT);
						toast.show();
						return;
					}
					mapView.setLogoPosition(positon);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.setScalControlsEnable(false);
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		addLogToView("Map Loaded");
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		addLogToView("Camera Chaging");
	}

	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
		// TODO Auto-generated method stub
		addLogToView("Camera Change Finish");
	}

	@Override
	public void onMapLongPress(LatLng arg0) {
		// TODO Auto-generated method stub
		addLogToView("Map Long Pressed");
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		if (etZoomLevel.isFocused()) {
			//点击地图时，清除editZoomLevel焦点
			etZoomLevel.clearFocus();
		}
		addLogToView("Map Clicked");
	}

	public void addLogToView(String log) {
		TextView textView = new TextView(this);
		textView.setTextColor(0xff000000);
		textView.setText(log);
		llLog.addView(textView);
		if (llLog.getChildCount() > 8) {
			llLog.removeViewAt(0);
		}
	}
}
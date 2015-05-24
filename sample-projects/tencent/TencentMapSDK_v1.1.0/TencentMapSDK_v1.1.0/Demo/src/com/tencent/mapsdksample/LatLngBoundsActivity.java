package com.tencent.mapsdksample;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.tencentmapsdksample.R;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.LatLngBounds;
import com.tencent.mapsdk.raster.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnMapHitListener;
import com.tencent.tencentmap.mapsdk.map.Projection;

public class LatLngBoundsActivity extends MapActivity {
	private MapView mapView;
	private Projection mProjection;
	private Button btnCalc;
	private Button btnGetBounds;
	private Button btnGetCustomBounds;
	private LatLngBounds mLatLngBounds;
	private LatLngBounds mCustomBounds;

	private GeoPoint gp1;
	private GeoPoint gp2;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_latlngbounds);
		init();
	}

	private void init() {
		mapView = (MapView)findViewById(R.id.map);
		mProjection = mapView.getProjection();
		btnCalc = (Button)findViewById(R.id.btn_calc);
		btnGetBounds = (Button)findViewById(R.id.btn_add_bounds);
		btnGetCustomBounds = (Button)findViewById(R.id.btn_get_custom_bounds);

		LatLng mapCenter = mapView.getMapCenter();
		double offset = 0.05; 
		final LatLng swLatLng = new LatLng(mapCenter.getLatitude() - offset, 
				mapCenter.getLongitude() - offset);
		final LatLng neLatLng = new LatLng(mapCenter.getLatitude() + offset, 
				mapCenter.getLongitude() + offset);
		//LatLng[0]为西南角，LatLng[1]为东北角
		final LatLng[] custBounds = new LatLng[2];

		btnCalc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnCalc.getText().toString().equals("计算距离")) {
					showToast("点击地图选取两点");
					btnCalc.setText("计算");
				} else {
					if (gp1 == null || gp2 == null) {
						showToast("请选点");
						return;
					}
					btnCalc.setText("计算距离");
					showToast("两点间距离为：" + mProjection.distanceBetween(gp1, gp2));
					gp1 = null;
					gp2 = null;
				}

			}
		});

		btnGetBounds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnCalc.getText().toString().equals("计算距离")) {
					if (btnGetBounds.getText().toString().equals("获取区域")) {
						mLatLngBounds = new LatLngBounds(swLatLng, neLatLng);
						drawBounds(swLatLng, neLatLng);
						showToast("点击地图，判断所点击的点是否在区域内");
						btnGetCustomBounds.setEnabled(true);
						btnGetBounds.setText("清除区域");
					} else {
						mapView.clearAllOverlays();
						custBounds[0] = null;
						custBounds[1] = null;
						btnGetCustomBounds.setEnabled(false);
						btnGetBounds.setText("获取区域");
						btnGetCustomBounds.setText("选择自定区域");
					}
				}
			}
		});

		btnGetCustomBounds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnGetCustomBounds.getText().toString().equals("选择自定区域")) {
					showToast("点击地图，获取包含所有点的区域，确认后判断区域间关系");
					
					btnGetCustomBounds.setText("确定");
				} else {
					if (mLatLngBounds.intersects(mCustomBounds)) {
						showToast("区域相交");
					} else if (mLatLngBounds.contains(mCustomBounds)) {
						showToast("包含自定义区域");
					} else {
						showToast("区域间无交点");
					}
					btnGetCustomBounds.setEnabled(false);
					btnGetCustomBounds.setText("选择自定区域");
				}
			}
		});

		mapView.getController().setOnMapHitListener(new OnMapHitListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				if (btnCalc.getText().toString().equals("计算")) {
					if (gp1 == null) {
						gp1 = new GeoPoint((int)(arg0.getLatitude() * 1e6),
								(int)(arg0.getLongitude() * 1e6));
						showToast("gp1:" + gp1.toString());
						return;
					} else if (gp2 == null) {
						gp2 = new GeoPoint((int)(arg0.getLatitude() * 1e6),
								(int)(arg0.getLongitude() * 1e6));
						showToast("gp2:" + gp2.toString());
						return;
					}
				} else if (btnGetBounds.getText().toString().equals("清除区域")
						 && btnGetCustomBounds.getText().toString().equals("选择自定区域")) {
					if (mLatLngBounds.contains(arg0)) {
						showToast("在区域内");
					} else {
						showToast("在区域外");
					}
				} else if (btnGetCustomBounds.getText().toString().equals("确定")) {
					if (custBounds[0] == null) {
						custBounds[0] = arg0;
						showToast("选择另一点");
					} else if (custBounds[1] == null) {
						if (isLatLngInSw(custBounds[0], arg0)) {
							custBounds[1] = arg0;
						} else if (isLatLngInNe(custBounds[0], arg0)) {
							custBounds[1] = new LatLng(custBounds[0].getLatitude(), custBounds[0].getLongitude());
							custBounds[0] = arg0;
						} else if (isLatLngInSe(custBounds[0], arg0)) {
							custBounds[1] = new LatLng(arg0.getLatitude(), custBounds[0].getLongitude());
							custBounds[0] = new LatLng(custBounds[0].getLatitude(), arg0.getLongitude());
						} else if (isLatLngInNw(custBounds[0], arg0)) {
							custBounds[1] = new LatLng(custBounds[0].getLatitude(), arg0.getLongitude());
							custBounds[0] = new LatLng(arg0.getLatitude(), custBounds[0].getLongitude());
						} else {
							showToast("请不要选择相对与第一点正南、正北、正东、正西方向的点");
							return;
						}
						mCustomBounds = new LatLngBounds(custBounds[0], custBounds[1]);
						drawBounds(custBounds[0], custBounds[1]);
					}
				}

			}
		});
	}

	/**
	 * 绘制区域
	 */
	private void drawBounds(LatLng sw, LatLng ne) {
		LatLng nw = new LatLng(ne.getLatitude(), sw.getLongitude());
		LatLng se = new LatLng(sw.getLatitude(), ne.getLongitude());
		mapView.addPolygon(new PolygonOptions()
		.add(nw)
		.add(ne)
		.add(se)
		.add(sw)
		.strokeWidth(0f)
		.fillColor(0x550000ff));
	}

	/**
	 * 判断latLng1是否在latLng2的西南方
	 */
	private boolean isLatLngInSw(LatLng latLng1, LatLng latLng2) {
		if (latLng1.getLatitude() < latLng2.getLatitude() 
				&& latLng1.getLongitude() < latLng2.getLongitude()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断latLng1是否在latLng2的东北方
	 */
	private boolean isLatLngInNe(LatLng latLng1, LatLng latLng2) {
		if (latLng1.getLatitude() > latLng2.getLatitude()
				&& latLng1.getLongitude() > latLng2.getLongitude()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断latLng1是否在latLng2的西北方
	 */
	private boolean isLatLngInNw(LatLng latLng1, LatLng latLng2) {
		if (latLng1.getLatitude() > latLng2.getLatitude()
				&& latLng1.getLongitude() < latLng2.getLongitude()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断latLng1是否在latLng2的东南方
	 */
	private boolean isLatLngInSe(LatLng latLng1, LatLng latLng2) {
		if (latLng1.getLatitude() < latLng2.getLatitude()
				&& latLng1.getLongitude() > latLng2.getLongitude()) {
			return true;
		}
		return false;
	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		toast.show();
	}
}

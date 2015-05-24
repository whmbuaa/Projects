package com.tencent.mapsdksample;

import com.example.tencentmapsdksample.R;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {
	
	public AppInfo appInfo;
	
	private static final DemoInfo[] demos = {
		new DemoInfo(R.string.demo_label_basemap, R.string.demo_desc_basemap, BasicMapActivity.class),
		new DemoInfo(R.string.demo_label_control, R.string.demo_desc_control, MapControlActivity.class),
		new DemoInfo(R.string.demo_label_overlay, R.string.demo_desc_overlay, MapOverlayActivity.class),
		new DemoInfo(R.string.demo_label_marker, R.string.demo_desc_marker, MarkerActivity.class),
		new DemoInfo(R.string.demo_label_mapfragment, R.string.demo_desc_mapfragment, MapFragmentActivity.class),
		new DemoInfo(R.string.demo_label_latlngbounds, R.string.demo_desc_latlngbounds, LatLngBoundsActivity.class)
	};
	private DemoListAdapter demoListAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	appInfo = AppInfo.getAppInfo(this.getPackageManager().
    				getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).
    				metaData.getString("TencentMapSDK"));
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			Toast toast = Toast.makeText(this, "未找到key", Toast.LENGTH_LONG);
			toast.show();
		}
        
        demoListAdapter = new DemoListAdapter();
        setListAdapter(demoListAdapter);
    } 

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	super.onListItemClick(l, v, position, id);
    	
    	Intent intent = new Intent(this, demos[position].demoActivityClass);
    	startActivity(intent);
    }
    
    private class DemoListAdapter extends BaseAdapter {
    	public DemoListAdapter () {
    		super();
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return demos.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return demos[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = View.inflate(MainActivity.this, R.layout.demo_list_item, null);
			TextView textViewLabel = (TextView)convertView.findViewById(R.id.label);
			TextView textViewdesc = (TextView)convertView.findViewById(R.id.desc);
			textViewLabel.setText(demos[position].label);
			textViewdesc.setText(demos[position].desc);
			
			return convertView;
		}
    	
    }
    
    private static class DemoInfo {
    	private final int label;
    	private final int desc;
    	private final Class<? extends android.app.Activity> demoActivityClass;
    	public DemoInfo(int lable, int desc, Class<? extends android.app.Activity> demoActivityClass) {
    		this.label = lable;
    		this.desc = desc;
    		this.demoActivityClass = demoActivityClass;
    	}
    }
    
    //只为获取key
    public static class AppInfo {
    	private final String USER_KEY;
    	private static AppInfo appInfo;
    	
    	private AppInfo(String key) {
    		USER_KEY = key;
    	}
    	public static AppInfo getAppInfo(String key) {
    		if (appInfo == null) {
    			appInfo = new AppInfo(key);
			}
    		return appInfo;
    	}
    	public static String getAppkey() {
    		return appInfo.USER_KEY;
    	}
    }
}

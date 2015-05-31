package com.paar.ch9;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;
    
    public LocalDataSource(Resources res) {
        if (res==null) throw new NullPointerException();
        
        createIcon(res);
    }
    
    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
        icon=BitmapFactory.decodeResource(res, R.drawable.poi_balloon);
    }
    
    
//    39.907190,116.408899
    
    public List<Marker> getMarkers() {
        Marker atl = new IconMarker("天安门",39.907190, 116.408899, 0, Color.DKGRAY, icon);
        cachedMarkers.add(atl);

       
        Marker m2 = new IconMarker("首都机场",40.081837,116.613520, 0, Color.DKGRAY, icon);
        cachedMarkers.add(m2);
        
        
       
        Marker m3 = new IconMarker("奥林匹克公园",40.019669, 116.402211, 0, Color.DKGRAY, icon);
        cachedMarkers.add(m3);
        
     
        Marker m4 = new IconMarker("石景山游乐园",39.911916, 116.207547, 0, Color.DKGRAY, icon);
        cachedMarkers.add(m4);
     
        
        Marker m5 = new IconMarker("中国传媒大学",39.910073, 116.568379, 0, Color.DKGRAY, icon);
        cachedMarkers.add(m5);
       
        
        Marker m6 = new IconMarker("世界花卉大观园",39.836033, 116.369080, 0, Color.DKGRAY, icon);
        cachedMarkers.add(m3);

        return cachedMarkers;
    }
}
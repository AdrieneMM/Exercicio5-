package com.example.alunos.mapmarker;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by alunos on 11/10/17.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UODATES = 10;

    private static final long MIN_TIME_BW_UODATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSTracker (Context mContext){
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){
            }else{
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UODATES,
                            MIN_DISTANCE_CHANGE_FOR_UODATES, this);
                    Log.d("Rede", "Rede");
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UODATES,
                                MIN_DISTANCE_CHANGE_FOR_UODATES, this);
                        Log.d("GPS Ativo", "GPS Ativo");
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();

        }
        return latitude;
    }
    public double getLongitude(){
        if(location != null){
            latitude = location.getLongitude();

        }
        return longitude;
    }
    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Configuração de GPS");
        alertDialog.setMessage("O GPS não está ativo. Deseja ir para as Configurações?");
        alertDialog.setPositiveButton("Configurações", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
        }

        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            public void onClick (DialogInterface dialog, int which){
            dialog.cancel();
        }

    });
        alertDialog.show();
    }

}

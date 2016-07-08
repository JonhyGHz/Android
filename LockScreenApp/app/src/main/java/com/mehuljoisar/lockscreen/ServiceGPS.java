package com.mehuljoisar.lockscreen;

import android.location.LocationListener;
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


public class ServiceGPS implements LocationListener {

    private final Context context;
    double latitud;
    double longitud;
    Location location;
    boolean gpsActivo;
    LocationManager locationManager;
    String ubicacion;

    // Constructor
    public ServiceGPS(Context c) {
        super();
        this.context = c;
        get_ubicacion();
    }

    public String get_ubicacion() {
        try {
            Log.e("EZH", "Servicio GPS - 1");
            locationManager = (LocationManager) this.context.getSystemService(context.LOCATION_SERVICE);
            Log.e("EZH", "Servicio GPS - 2");
            gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.e("EZH", "Servicio GPS - 3");
            if (gpsActivo)// gps activado
            {
                Log.e("EZH", "Servicio GPS - 4");
                Log.e("EZH", "Servicio GPS - 4.5");
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);

                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                Log.e("EZH","Servicio GPS - 5");
                ubicacion = "Long => "+String.valueOf(location.getLongitude())+", Lat => "+String.valueOf(location.getLatitude());
                Log.e("EZH","Servicio GPS - 6");


                //ubicacion = "http://maps.google.com/?ll="+location.getLongitude()+","+location.getLatitude();
                //Log.e("EZH","Servicio GPS - 6");
            }
            else
            {
                // activar gps
                ubicacion = "Activar GPS";
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e("EZH","Error en get_ubicacion => "+e.getMessage());
            ubicacion = "";
        }


        return ubicacion;
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
}

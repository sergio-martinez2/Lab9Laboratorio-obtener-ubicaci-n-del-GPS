package com.example.login_php.funcionalidades;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class OdometerService extends Service {
    private final IBinder binder = new OdometerBinder();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double distanceInMeters = 0.0;
    private Location lastLocation = null;

    public static final String ACTION_UPDATE_DISTANCE = "com.example.login_php.UPDATE_DISTANCE";
    public static final String EXTRA_DISTANCE = "com.example.login_php.EXTRA_DISTANCE";

    public class OdometerBinder extends Binder {
        OdometerService getService() {
            return OdometerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation != null) {
                    distanceInMeters += location.distanceTo(lastLocation);
                }
                lastLocation = location;

                // Enviar broadcast con la distancia actualizada
                Intent updateIntent = new Intent(ACTION_UPDATE_DISTANCE);
                updateIntent.putExtra(EXTRA_DISTANCE, distanceInMeters);
                sendBroadcast(updateIntent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    public double getDistance() {
        return distanceInMeters;
    }
}

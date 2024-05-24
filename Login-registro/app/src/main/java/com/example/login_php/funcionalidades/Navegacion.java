package com.example.login_php.funcionalidades;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_php.R;

public class Navegacion extends AppCompatActivity {

    private OdometerService odometerService;
    private boolean bound = false;
    private TextView distanceTextView;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OdometerService.OdometerBinder binder = (OdometerService.OdometerBinder) service;
            odometerService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private BroadcastReceiver distanceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (OdometerService.ACTION_UPDATE_DISTANCE.equals(intent.getAction())) {
                double distance = intent.getDoubleExtra(OdometerService.EXTRA_DISTANCE, 0);
                distanceTextView.setText(String.format("Distance: %.2f meters", distance));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        distanceTextView = findViewById(R.id.distanceTextView);

        // Iniciar y enlazar el servicio de odómetro
        Intent odometerIntent = new Intent(this, OdometerService.class);
        startService(odometerIntent);
        bindService(odometerIntent, connection, Context.BIND_AUTO_CREATE);

        // Obtener los datos de la carga seleccionada del Intent
        Intent intent = getIntent();
        String ciudadOrigen = intent.getStringExtra("ciudadOrigen");
        String ciudadDestino = intent.getStringExtra("ciudadDestino");

        // Extraer las coordenadas de las cadenas de texto
        double latOrigen = extractLatitude(ciudadOrigen);
        double lngOrigen = extractLongitude(ciudadOrigen);
        double latDestino = extractLatitude(ciudadDestino);
        double lngDestino = extractLongitude(ciudadDestino);

        // Verificar que las coordenadas sean válidas
        if (latOrigen == 0 || lngOrigen == 0 || latDestino == 0 || lngDestino == 0) {
            Toast.makeText(this, "Error: Coordenadas inválidas", Toast.LENGTH_SHORT).show();
            finish(); // Finalizar la actividad si las coordenadas son inválidas
            return;
        }

        // Crear una URI con las coordenadas de origen y destino
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latDestino + "," + lngDestino + "&origin=" + latOrigen + "," + lngOrigen);

        // Crear un Intent para abrir Google Maps con las coordenadas especificadas
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Especificar que se utilice Google Maps

        // Verificar si hay una aplicación que pueda manejar el Intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent); // Abrir Google Maps
        } else {
            Toast.makeText(this, "Error: No se encontró Google Maps", Toast.LENGTH_SHORT).show();
        }

        // Finalizar la actividad después de iniciar Google Maps
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Enlazar el servicio si no está enlazado
        if (!bound) {
            Intent intent = new Intent(this, OdometerService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
        // Registrar el receptor de broadcasts
        registerReceiver(distanceReceiver, new IntentFilter(OdometerService.ACTION_UPDATE_DISTANCE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
        // Desregistrar el receptor de broadcasts
        unregisterReceiver(distanceReceiver);
    }

    // Método para extraer la latitud de la cadena de texto
    private double extractLatitude(String coordinateString) {
        if (coordinateString != null && coordinateString.contains("lat/lng:")) {
            int startIndex = coordinateString.indexOf("(") + 1;
            int commaIndex = coordinateString.indexOf(",");
            String latitudeString = coordinateString.substring(startIndex, commaIndex);
            return Double.parseDouble(latitudeString);
        }
        return 0;
    }

    // Método para extraer la longitud de la cadena de texto
    private double extractLongitude(String coordinateString) {
        if (coordinateString != null && coordinateString.contains("lat/lng:")) {
            int commaIndex = coordinateString.indexOf(",") + 1;
            int endIndex = coordinateString.indexOf(")");
            String longitudeString = coordinateString.substring(commaIndex, endIndex);
            return Double.parseDouble(longitudeString);
        }
        return 0;
    }
}
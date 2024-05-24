package com.example.login_php.homes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.login_php.Database;
import com.example.login_php.R;
import com.example.login_php.funcionalidades.CalificarViajeActivity;
import com.example.login_php.funcionalidades.NotificarVehiculo;
import com.example.login_php.funcionalidades.NotificationHelper;
import com.example.login_php.funcionalidades.crearCarga;
import com.example.login_php.funcionalidades.gestionCargas;
import com.example.login_php.funcionalidades.historicoCargas;
import com.example.login_php.objetos.Vehiculo;

import java.util.List;

public class homecliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homecliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Database database = new Database(this);

        String email = getIntent().getStringExtra("CORREO_ELECTRONICO");
        String nombreUsuario = obtenerNombreUsuario(database, email);

        TextView textView = findViewById(R.id.inicioUsuario);
        textView.setText("Bienvenido, cliente " + nombreUsuario + "!");

        Button btnCrearCarga = findViewById(R.id.btn_crearcarga);
        Button btnGestionCargas = findViewById(R.id.btn_gestion_de_cargas);
        Button btnHistoricoCargas = findViewById(R.id.btn_historico_de_cargas);
        Button btnCalificarViaje = findViewById(R.id.btn_calificar_viaje);

        btnCrearCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), crearCarga.class);
                intent.putExtra("CORREO_ELECTRONICO", email);
                startActivity(intent);
            }
        });

        btnGestionCargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), gestionCargas.class);
                startActivity(intent);
            }
        });

        btnHistoricoCargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homecliente.this, historicoCargas.class));
            }
        });

        btnCalificarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homecliente.this, CalificarViajeActivity.class);
                startActivity(intent);
            }
        });
        NotificarVehiculo.createNotificationChannel(this);
        NotificarVehiculo.showNewRequestNotification(this);
    }

    // Método obtener el nombre del usuario desde la base de datos
    private String obtenerNombreUsuario(Database database, String email) {
        String nombreUsuario = database.getUserName(email);
        return nombreUsuario;
    }

    // Método para generar una notificación

}

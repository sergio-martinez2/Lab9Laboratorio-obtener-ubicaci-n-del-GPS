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

public class homeconductor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeconductor);

        Button btnListaVehiculosDisponibles, btnVerPerfil , btnversolicitudcargas, btnEstadodelviaje;
        // Configurar la barra de herramientas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Inicializar la base de datos
        Database database = new Database(this);

        // Obtener el correo electrónico del usuario de la actividad anterior
        String email = getIntent().getStringExtra("CORREO_ELECTRONICO");

        // Obtener el nombre del usuario de la base de datos
        String nombreUsuario = database.getUserName(email);

        // Mostrar el nombre del usuario en un TextView
        TextView textView = findViewById(R.id.inicioUsuario);
        textView.setText("Bienvenido, conductor " + nombreUsuario + "!");

        // Configurar el botón para ver el perfil
        btnVerPerfil = findViewById(R.id.btn_ver_perfil);
        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad para ver el perfil
                Intent intent = new Intent(getApplicationContext(), com.example.login_php.funcionalidades.verPerfil.class);
                intent.putExtra("CORREO_ELECTRONICO", email); // Aquí agregas el correo electrónico como extra
                startActivity(intent);

            }
        });
        btnListaVehiculosDisponibles = findViewById(R.id.btn_ver_lista_vehiculos);
        btnListaVehiculosDisponibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad ListaVehiculosDisponiblesActivity
                Intent intent = new Intent(getApplicationContext(), com.example.login_php.funcionalidades.verListaVehiculos.class);
                startActivity(intent);
            }
        });
btnversolicitudcargas = findViewById(R.id.btn_ver_solicitud_cargas);
        btnversolicitudcargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.login_php.funcionalidades.gestionCargas.class);
                // intent.putExtra("CORREO_ELECTRONICO", email);
                // OPCIONAL SI SE NECESITA ALGUN PARAMETRO
                startActivity(intent);
            }
        });





    }
}

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
import com.example.login_php.funcionalidades.registrarVehiculo;
import com.example.login_php.funcionalidades.verListaVehiculos;

public class homeadministrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeadministrador);

        Toolbar toolbar = findViewById(R.id.toolbar); // Inflar el Toolbar desde el layout
        setSupportActionBar(toolbar);  // Configurar el Toolbar como la ActionBar de la actividad
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ocultar el título del ActionBar
        Database database = new Database(this);  // Obtener una instancia de la base de datos
        String email = getIntent().getStringExtra("CORREO_ELECTRONICO");
        String nombreUsuario = obtenerNombreUsuario(database, email); // Obtener el nombre del usuario desde la base de datos

        // Mostrar el nombre del usuario en un TextView
        TextView textView = findViewById(R.id.inicioUsuario);
        textView.setText("Bienvenido, cliente " + nombreUsuario + "!");



        // Configurar el evento de clic para el botón de registrar vehículo
        Button btnRegistrarVehiculo = findViewById(R.id.btn_registrar_vehiculo);
        btnRegistrarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActivityRegistrarVehiculo();
            }
        });

        // Configurar el evento de clic para el botón de ver lista de vehículos
        Button btnVerListaVehiculos = findViewById(R.id.btn_ver_lista_vehiculos);
        btnVerListaVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActivityVerListaVehiculos();
            }
        });
    }

    // Método para abrir la actividad de registrar vehículo
    private void abrirActivityRegistrarVehiculo() {
        Intent intent = new Intent(this, com.example.login_php.funcionalidades.registrarVehiculo.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de ver lista de vehículos
    private void abrirActivityVerListaVehiculos() {
        Intent intent = new Intent(this, com.example.login_php.funcionalidades.verListaVehiculos.class);
        startActivity(intent);
    }

    // Método obtener el nombre del usuario desde la base de datos
    private String obtenerNombreUsuario(Database database, String email) {
        String nombreUsuario = database.getUserName(email);

        // Retornar el nombre del usuario obtenido
        return nombreUsuario;
    }

}



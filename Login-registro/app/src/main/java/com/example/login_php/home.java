package com.example.login_php;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.login_php.funcionalidades.NotificationHelper;
import com.example.login_php.funcionalidades.registrarVehiculo;
import com.example.login_php.funcionalidades.verListaVehiculos;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        // Obtener una instancia de la base de datos
        Database database = new Database(this);

        // Obtener el nombre del usuario desde la base de datos
        String nombreUsuario = obtenerNombreUsuario(database);

        // Mostrar el nombre del usuario en un TextView
        TextView textView = findViewById(R.id.IncioUsuario);
        textView.setText("Bienvenido, " + nombreUsuario + "!");

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
        Intent intent = new Intent(this, registrarVehiculo.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de ver lista de vehículos
    private void abrirActivityVerListaVehiculos() {
        Intent intent = new Intent(this, verListaVehiculos.class);
        startActivity(intent);
    }

    // Método para obtener el nombre del usuario desde la base de datos
    private String obtenerNombreUsuario(Database database) {
        // Implementa la lógica necesaria para obtener el nombre del usuario
        return "Usuario";
    }


}

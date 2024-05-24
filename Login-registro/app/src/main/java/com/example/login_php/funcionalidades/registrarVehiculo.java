package com.example.login_php.funcionalidades;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.login_php.Database;
import com.example.login_php.R;

public class registrarVehiculo extends AppCompatActivity {

    // Variables para los elementos de la interfaz de usuario
    private EditText etMarca, etModelo, etPlaca, etColor;
    private Spinner spinnerEstado;
    private Button btnRegistrar;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarvehiculos);

        // Inicializar los elementos de la interfaz de usuario
        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etPlaca = findViewById(R.id.etPlaca);
        etColor = findViewById(R.id.etColor);
        btnRegistrar = findViewById(R.id.btn_register);
        spinnerEstado = findViewById(R.id.spinnerEstado);

        database = new Database(this);

        // Cargar opciones al Spinner desde strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_estado_vehiculo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // Configurar el listener para el botón de registro
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener la posición seleccionada del spinner
                int seleccion = spinnerEstado.getSelectedItemPosition();

                // Verificar si la posición seleccionada es la opción por defecto
                if (seleccion == 0) {
                    // Mostrar un mensaje de error
                    Toast.makeText(getApplicationContext(), "Por favor selecciona un estado Inicial válido", Toast.LENGTH_SHORT).show();
                } else {
                    // Si la opción seleccionada es válida, realizar la inserción de datos
                    registrarVehiculo();
                }
            }
        });

    }


    // Método para registrar el vehículo
    private void registrarVehiculo() {
        // Obtener los datos ingresados por el usuario
        String marca = etMarca.getText().toString().trim();
        String modelo = etModelo.getText().toString().trim();
        String placa = etPlaca.getText().toString().trim();
        String color = etColor.getText().toString().trim();
        String estado = spinnerEstado.getSelectedItem().toString();

        // Guardar los datos en tu base de datos
        database.insertVehiculo(marca, modelo, placa, color, estado);


        // Mostrar un mensaje de confirmación
        Toast.makeText(this, "Vehículo registrado correctamente", Toast.LENGTH_SHORT).show();
    }

}

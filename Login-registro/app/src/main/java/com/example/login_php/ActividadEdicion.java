package com.example.login_php;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_php.objetos.Vehiculo;

public class ActividadEdicion extends AppCompatActivity {
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_edicion);
        database = new Database(this);
        int vehiculoId = getIntent().getIntExtra("vehiculoId", -1);

        // Obtener el objeto Vehiculo seleccionado de los extras del intent
        Vehiculo vehiculoSeleccionado = obtenerVehiculoPorId(vehiculoId);

        // Obtener referencias a tus EditText y Spinner en el layout XML
        EditText editTextMarca = findViewById(R.id.etMarca);
        EditText editTextModelo = findViewById(R.id.etModelo);
        EditText editTextPlaca = findViewById(R.id.etPlaca);
        EditText editTextColor = findViewById(R.id.etColor);
        Spinner spinnerEstado = findViewById(R.id.spinnerEstado);

        // Mostrar la información del vehículo en los EditText y Spinner
        editTextMarca.setText(vehiculoSeleccionado.getMarca());
        editTextModelo.setText(vehiculoSeleccionado.getModelo());
        editTextPlaca.setText(vehiculoSeleccionado.getPlaca());
        editTextColor.setText(vehiculoSeleccionado.getColor());
        // Establecer el estado del vehículo seleccionado en el Spinner
        spinnerEstado.setSelection(obtenerPosicionEstado(vehiculoSeleccionado.getEstado()));

        // Permitir al usuario editar la información y guardar los cambios
        Button buttonGuardar = findViewById(R.id.btn_Guardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos valores de los EditText
                String nuevaMarca = editTextMarca.getText().toString();
                String nuevoModelo = editTextModelo.getText().toString();
                String nuevaPlaca = editTextPlaca.getText().toString();
                String nuevoColor = editTextColor.getText().toString();
                String nuevoEstado = spinnerEstado.getSelectedItem().toString();

                // Actualizar el vehículo con los nuevos valores
                vehiculoSeleccionado.setMarca(nuevaMarca);
                vehiculoSeleccionado.setModelo(nuevoModelo);
                vehiculoSeleccionado.setPlaca(nuevaPlaca);
                vehiculoSeleccionado.setColor(nuevoColor);
                vehiculoSeleccionado.setEstado(nuevoEstado);

                // Actualizar el vehículo en la base de datos
                database.actualizarVehiculo(vehiculoSeleccionado);

                // Mostrar un mensaje indicando que los cambios han sido guardados
                Toast.makeText(ActividadEdicion.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener la posición del estado en el Spinner
    private int obtenerPosicionEstado(String estado) {
        String[] opcionesEstadoVehiculo = getResources().getStringArray(R.array.opciones_estado_vehiculo);
        for (int i = 0; i < opcionesEstadoVehiculo.length; i++) {
            if (opcionesEstadoVehiculo[i].equals(estado)) {
                return i;
            }
        }
        return 0; // Por defecto, seleccionar la primera opción si no se encuentra ninguna coincidencia
    }

    // Método para obtener un vehículo de la base de datos por su ID
    private Vehiculo obtenerVehiculoPorId(int id) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("VEHICULOS", null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        Vehiculo vehiculo = null;
        if (cursor != null && cursor.moveToFirst()) {
            String marca = cursor.getString(cursor.getColumnIndex("MARCA"));
            String modelo = cursor.getString(cursor.getColumnIndex("MODELO"));
            String placa = cursor.getString(cursor.getColumnIndex("PLACA"));
            String color = cursor.getString(cursor.getColumnIndex("COLOR"));
            String estado = cursor.getString(cursor.getColumnIndex("ESTADO"));
            vehiculo = new Vehiculo(id, marca, modelo, placa, color, estado);
            cursor.close();
        }
        db.close();
        return vehiculo;
    }
}

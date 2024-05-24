package com.example.login_php.funcionalidades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_php.Database;
import com.example.login_php.R;
import com.example.login_php.objetos.Vehiculo;

public class ActividadEdicion extends AppCompatActivity {
    private Database database;
    private Spinner spinnerEstado; // Declarar el Spinner fuera del método onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_edicion);
        database = new Database(this);
        int vehiculoId = getIntent().getIntExtra("vehiculoId", -1);

        // Aquí puedes obtener el objeto Vehiculo seleccionado de los extras del intent
        Vehiculo vehiculoSeleccionado = obtenerVehiculoPorId(vehiculoId);

        // Obtener referencias a tus EditText en el layout XML
        EditText editTextMarca = findViewById(R.id.etMarca);
        EditText editTextModelo = findViewById(R.id.etModelo);
        EditText editTextPlaca = findViewById(R.id.etPlaca);
        EditText editTextColor = findViewById(R.id.etColor);
        spinnerEstado = findViewById(R.id.spinnerEstado); // Asignar la referencia del Spinner

        // Mostrar la información del vehículo en los EditText
        editTextMarca.setText(vehiculoSeleccionado.getMarca());
        editTextModelo.setText(vehiculoSeleccionado.getModelo());
        editTextPlaca.setText(vehiculoSeleccionado.getPlaca());
        editTextColor.setText(vehiculoSeleccionado.getColor());

        // Permitir al usuario editar la información y guardar los cambios
        Button buttonGuardar = findViewById(R.id.btn_Guardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos valores de los EditText y Spinner
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

                // Aquí puedes llamar a un método en tu base de datos para actualizar el vehículo
                database.actualizarVehiculo(vehiculoSeleccionado);

                // Indicar al usuario que los cambios han sido guardados (puedes usar Toast o Snackbar)
                Toast.makeText(ActividadEdicion.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Vehiculo obtenerVehiculoPorId(int id) {
        // Obtener una instancia de la base de datos
        Database database = new Database(this); // Ajusta esto según cómo obtengas la instancia de tu base de datos

        // Consultar la base de datos para obtener el vehículo por su ID
        Vehiculo vehiculo = null;
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("VEHICULOS", // Tabla
                null, // Todas las columnas
                "_id=?", // Clausula WHERE
                new String[]{String.valueOf(id)}, // Valores de la clausula WHERE
                null, null, null);

        // Verificar si se encontró un vehículo con el ID dado
        if (cursor != null && cursor.moveToFirst()) {
            // Obtener los datos del cursor
            String marca = cursor.getString(cursor.getColumnIndex("MARCA"));
            String modelo = cursor.getString(cursor.getColumnIndex("MODELO"));
            String placa = cursor.getString(cursor.getColumnIndex("PLACA"));
            String color = cursor.getString(cursor.getColumnIndex("COLOR"));
            String estado = cursor.getString(cursor.getColumnIndex("ESTADO"));

            // Construir el objeto Vehiculo
            vehiculo = new Vehiculo(id, marca, modelo, placa, color, estado);

            // Cerrar el cursor
            cursor.close();
        }

        // Cerrar la conexión de la base de datos
        db.close();

        // Devolver el vehículo encontrado o null si no se encontró ninguno
        return vehiculo;
    }
}

    package com.example.login_php.funcionalidades;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import com.example.login_php.Database;
    import com.example.login_php.R;
    import com.google.android.gms.maps.model.LatLng;

    public class crearCarga extends AppCompatActivity {

        private Button btnSeleccionarUbicacionOrigen, btnSeleccionarUbicacionDestino, btnRegistrarCarga;
        private String ubicacionOrigen, ubicacionDestino, nombreDeCarga, estado, creadorCarga; // Variables para almacenar las ubicaciones
        private EditText editnombrecarga, editpesotonelada;

        private Integer peso;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crearcarga);

            editnombrecarga = findViewById(R.id.etNombredeCarga);
            editpesotonelada = findViewById(R.id.etPeso);
            btnSeleccionarUbicacionOrigen = findViewById(R.id.btnSeleccionarUbicacionOrigen);
            btnSeleccionarUbicacionDestino = findViewById(R.id.btnSeleccionarUbicacionDestino);
            btnRegistrarCarga = findViewById(R.id.btn_register);

            Database database = new Database(this);

            // Crear el canal de notificaciones
            NotificationHelper.createNotificationChannel(this);

            btnSeleccionarUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Abrir la actividad de mapas y permitir al usuario seleccionar la ubicación de origen
                    Intent intent = new Intent(crearCarga.this, Mapas.class);
                    startActivityForResult(intent, 1);
                }
            });

            btnSeleccionarUbicacionDestino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Abrir la actividad de mapas y permitir al usuario seleccionar la ubicación de destino
                    Intent intent = new Intent(crearCarga.this, Mapas.class);
                    startActivityForResult(intent, 2);
                }
            });

            btnRegistrarCarga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nombreDeCarga = editnombrecarga.getText().toString();
                    estado = "Disponible"; // Por ejemplo, asigna un estado por defecto

                    Intent intent = getIntent();

                    // Reemplaza "correo@example.com" con el correo del usuario logueado
                    String emailUsuarioLogueado = intent.getStringExtra("CORREO_ELECTRONICO");

                    creadorCarga = obtenerNombreUsuarioLogueado(database, emailUsuarioLogueado);

                    if (creadorCarga == null) {
                        Toast.makeText(getApplicationContext(), "Error: No se pudo obtener el nombre del creador de la carga", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        peso = Integer.parseInt(editpesotonelada.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "El peso debe ser un número válido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (ubicacionOrigen == null || ubicacionDestino == null) {
                        Toast.makeText(getApplicationContext(), "Por favor, selecciona las ubicaciones de origen y destino", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Realizar la inserción en la base de datos con las ubicaciones seleccionadas
                    database.insertCarga(nombreDeCarga, peso, ubicacionOrigen, ubicacionDestino, estado, creadorCarga);

                    // Mostrar una notificación de nueva carga
                    NotificationHelper.showNewRequestNotification(crearCarga.this);

                    // Ejemplo: Mostrar un mensaje de éxito
                    Toast.makeText(crearCarga.this, "Carga registrada", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    // Seleccionar ubicación de origen
                    LatLng ubicacion = data.getParcelableExtra("ubicacion");
                    ubicacionOrigen = ubicacion.toString();
                    Toast.makeText(getApplicationContext(), "Ubicación de origen seleccionada: " + ubicacionOrigen, Toast.LENGTH_SHORT).show();
                } else if (requestCode == 2) {
                    // Seleccionar ubicación de destino
                    LatLng ubicacion = data.getParcelableExtra("ubicacion");
                    ubicacionDestino = ubicacion.toString();
                    Toast.makeText(getApplicationContext(), "Ubicación de destino seleccionada: " + ubicacionDestino, Toast.LENGTH_SHORT).show();
                }
            }
        }

        // Método obtener el nombre del usuario desde la base de datos
        private String obtenerNombreUsuarioLogueado(Database database, String email) {
            String nombreUsuario = database.getUserName(email);
            // Retornar el nombre del usuario obtenido
            return nombreUsuario;
        }
    }

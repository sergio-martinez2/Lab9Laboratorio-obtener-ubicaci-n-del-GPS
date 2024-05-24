package com.example.login_php.funcionalidades;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.login_php.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CalificarViajeActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button btnEnviarCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_viaje);

        // Inicializar vistas
      ratingBar = findViewById(R.id.ratingBar);
      btnEnviarCalificacion = findViewById(R.id.btnEnviarCalificacion);

        // Configurar botón de enviar calificación
        btnEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCalificacion();
            }
        });
    }

    public void enviarCalificacion() {
        float calificacion = ratingBar.getRating();
        if (calificacion == 0) {
            Toast.makeText(this, "Por favor, selecciona una calificación", Toast.LENGTH_SHORT).show();
        } else {
            // Aquí puedes agregar la lógica para enviar la calificación al servidor o base de datos
            Toast.makeText(this, "Calificación enviada: " + calificacion, Toast.LENGTH_SHORT).show();
            // Finaliza la actividad y regresa a la anterior
            finish();
        }
    }
}

package com.example.login_php.funcionalidades;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_php.Database;
import com.example.login_php.R;

public class verPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        // Inicializar la base de datos
        Database database = new Database(this);

        String email = getIntent().getStringExtra("CORREO_ELECTRONICO");


        // Obtener el nombre de usuario y calificación de la base de datos
        String nombreUsuario = database.getUserName(email); // Reemplaza esto por el método adecuado para obtener el nombre de usuario
        float calificacion = database.getUserRating(email); // Reemplaza esto por el método adecuado para obtener la calificación del usuario
        String comentario = database.getUserComment(email); // Reemplaza esto por el método adecuado para obtener el comentario del usuario

        // Enlazar las vistas
        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewCalificacion = findViewById(R.id.textViewCalificacion);
        TextView textViewComentario = findViewById(R.id.textViewComentario);

        // Establecer los valores en las vistas
        textViewNombre.setText(nombreUsuario);
        textViewCalificacion.setText("Calificación: " + calificacion);
        textViewComentario.setText("Comentario: " + comentario);
    }
}

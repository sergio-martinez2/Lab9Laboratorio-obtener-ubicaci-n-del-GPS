package com.example.login_php;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login_php.funcionalidades.NotificationHelper;

public class login extends AppCompatActivity {

    EditText email, contraseña;
    String tipoUsuario;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.etemail);
        contraseña = findViewById(R.id.etcontraseña);

        database = new Database(this);
        Button btnLogin = findViewById(R.id.btn_login);

        // Crear el canal de notificaciones
        NotificationHelper.createNotificationChannel(this);

        // Mostrar una notificación de nueva solicitud de carga al abrir la app
        NotificationHelper.showNewRequestNotification(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí colocarías el código que deseas ejecutar cuando se haga clic en el botón
                // Por ejemplo, podrías llamar al método Login que has definido en tu actividad
                Login(v);
            }
        });

    }

    public void Login(View v) {
        String str_email = email.getText().toString().trim();
        String str_password = contraseña.getText().toString().trim();

        if (str_email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (str_password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            if (database.checkUser(str_email, str_password)) {
                String userType = database.getUserType(str_email); // Obtener el tipo de usuario después de verificar la autenticación
                email.setText("");
                contraseña.setText("");
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                // Crear un Intent y agregar el correo electrónico como extra
                Intent intent = new Intent(getApplicationContext(), com.example.login_php.homes.homecliente.class);
                intent.putExtra("CORREO_ELECTRONICO", str_email);


                ejecutarhome(userType, intent);
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ejecutarhome(String userType, Intent intent){
        // Determinar qué actividad iniciar en función del tipo de usuario
        switch (userType) {
            case "Administrador":
                intent.setClass(getApplicationContext(), com.example.login_php.homes.homeadministrador.class);
                break;
            case "Cliente":
                intent.setClass(getApplicationContext(), com.example.login_php.homes.homecliente.class);
                break;
            case "Conductor":
                intent.setClass(getApplicationContext(), com.example.login_php.homes.homeconductor.class);
                break;
            default:
                // En caso de un tipo de usuario desconocido, iniciar la actividad predeterminada
                intent.setClass(getApplicationContext(), login.class);
                break;
        }

        startActivity(intent);
    }

    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), registrar.class));
        finish();
    }
}

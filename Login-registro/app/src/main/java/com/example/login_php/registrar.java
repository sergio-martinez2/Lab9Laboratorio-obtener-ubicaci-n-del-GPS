package com.example.login_php;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class registrar extends AppCompatActivity {
    EditText txtName, txtEmail, pass, passConfirm; // Agregamos la referencia al campo de confirmación de contraseña
    Spinner spinner;
    Button btn_insert;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtName = findViewById(R.id.ednombre);
        txtEmail = findViewById(R.id.etemail);
        pass = findViewById(R.id.etcontraseña);
        passConfirm = findViewById(R.id.etcontraseña2); // Referencia al campo de confirmación de contraseña
        btn_insert = findViewById(R.id.btn_register);
        spinner = findViewById(R.id.spinnerTipo);
        txtEmail.setFilters(new InputFilter[] {new LowercaseInputFilter()});


        database = new Database(this);

        Spinner spinnerTipo = findViewById(R.id.spinnerTipo);
        String[] opciones = {"Selecciona el tipo de usuario", "Administrador", "Cliente", "Conductor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
        spinnerTipo.setSelection(0); // Establecer "Selecciona el tipo de usuario" como la opción seleccionada por defecto

        // VALIDACION DE EMAIL
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se utiliza
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se utiliza
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Verificar si el correo electrónico es válido
                String email = txtEmail.getText().toString().trim();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtEmail.setError("Ingrese un correo electrónico válido");
                    return; // Salir del método si el correo no es válido
                } else {
                    txtEmail.setError(null); // Limpiar el error si el formato es correcto
                }

                // Verificar si el texto contiene un "@" y termina con ".com"
                if (email.contains("@") && email.endsWith(".com")) {
                    // Verificar si el texto no contiene ningún punto después del "@" pero sí contiene al menos uno antes del "@"
                    int indexOfAt = email.indexOf("@");
                    int indexOfDot = email.indexOf(".", indexOfAt); // Buscar el primer punto después del "@"
                    if (indexOfDot == -1 && email.substring(0, indexOfAt).contains(".")) {
                        // Eliminar el último carácter (punto)
                        txtEmail.setText(email.substring(0, email.length() - 1));
                        txtEmail.setSelection(txtEmail.getText().length()); // Colocar el cursor al final del texto
                    }
                }
            }
        });


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener la posición seleccionada del spinner
                int seleccion = spinner.getSelectedItemPosition();

                // Verificar si la posición seleccionada es la opción por defecto
                if (seleccion == 0) {
                    // Mostrar un mensaje de error
                    Toast.makeText(getApplicationContext(), "Por favor selecciona un tipo de usuario válido", Toast.LENGTH_SHORT).show();
                } else {
                    // Si la opción seleccionada es válida, realizar la inserción de datos
                    insertData();
                }

            }
        });
    }

    private void insertData() {
        String nombre = txtName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String confirmPassword = passConfirm.getText().toString().trim(); // Obtenemos el valor del campo de confirmación de contraseña
        String tipoUsuario = spinner.getSelectedItem().toString();

        if (nombre.isEmpty()) {
            txtName.setError("Complete los campos");
            return;
        } else if (email.isEmpty()) {
            txtEmail.setError("Complete los campos");
            return;
        } else if (password.isEmpty()) {
            pass.setError("Complete los campos");
            return;
        } else if (confirmPassword.isEmpty()) {
            passConfirm.setError("Complete los campos");
            return;
        } else if (!password.equals(confirmPassword)) { // Comparamos las contraseñas y mostramos un mensaje si no coinciden
            passConfirm.setError("Las contraseñas no coinciden");
            return;
        }

        // Verificar si el usuario ya existe en la base de datos
        if (database.existeUsuario(email)) {
            // El usuario ya existe, mostrar mensaje de error
            Toast.makeText(getApplicationContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
            return;
        }
        // Si no existe, realizar la inserción de datos
        database.insertUser(database.getWritableDatabase(), nombre, email, password, tipoUsuario);

        Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(registrar.this, login.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }
}

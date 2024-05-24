package com.example.login_php.funcionalidades;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_php.Database;
import com.example.login_php.R;
import com.example.login_php.objetos.Carga;
import java.util.ArrayList;
import java.util.List;

public class historicoCargas extends AppCompatActivity {

    private List<Carga> listaCargas;
    private ArrayAdapter<Carga> adapter;
    private ListView listViewCargas;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_cargas);

        listViewCargas = findViewById(R.id.listViewCargas);
        listaCargas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCargas);
        listViewCargas.setAdapter(adapter);

        // Inicializar la base de datos
        database = new Database(this);

        // Cargar las cargas desde la base de datos
        cargarCargasDesdeBaseDeDatos();

        // Agregar el listener de clic largo al ListView
        registerForContextMenu(listViewCargas);
    }

    private void cargarCargasDesdeBaseDeDatos() {
        // Realizar una consulta SQL para obtener las cargas confirmadas
        List<Carga> cargasConfirmadas = database.obtenerCargasConEstado("Confirmada");

        // Limpiar la lista actual de cargas y agregar las cargas recuperadas
        listaCargas.clear();
        listaCargas.addAll(cargasConfirmadas);

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
    }



    // Método para crear el menú contextual para editar o eliminar una carga
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_historico_de_cargas, menu);
    }

    // Método para manejar las acciones de los elementos del menú contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Carga cargaSeleccionada = listaCargas.get(info.position);

        switch (item.getItemId()) {
            case R.id.calificar:
                // Aquí puedes abrir la actividad de edición de carga
                // Puedes pasar la carga seleccionada como extra en el intent
                // Implementa la actividad de edición de carga (ActividadEdicionCarga) similar a la de vehículos
                return true;

            case R.id.editar:
                // Aquí puedes abrir la actividad de edición de carga
                Intent intent = new Intent(historicoCargas.this, ActividadEdicionCarga.class);
                intent.putExtra("cargaId", cargaSeleccionada.getId());
                startActivity(intent);
                return true;

            case R.id.eliminar:
                // Mostrar un diálogo de confirmación antes de eliminar la carga
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar carga")
                        .setMessage("¿Estás seguro de que deseas eliminar esta carga?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Eliminar la carga de la base de datos y recargar la lista
                                database.eliminarCarga(cargaSeleccionada);
                                cargarCargasDesdeBaseDeDatos();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
                return true;

            case R.id.actualizar:
                cargarCargasDesdeBaseDeDatos();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}

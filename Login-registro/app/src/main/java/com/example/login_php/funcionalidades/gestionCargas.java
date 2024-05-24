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

public class gestionCargas extends AppCompatActivity {

    private List<Carga> listaCargas;
    private ArrayAdapter<Carga> adapter;
    private ListView listViewCargas;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cargas);

        listViewCargas = findViewById(R.id.listViewCargas);
        listaCargas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCargas);
        listViewCargas.setAdapter(adapter);

        // Inicializar la base de datos
        database = new Database(this);

        // Cargar las cargas desde la base de datos
        cargarCargasDesdeBaseDeDatosExceptoConfirmadas();

        // Agregar el listener de clic largo al ListView
        registerForContextMenu(listViewCargas);
    }

    private void cargarCargasDesdeBaseDeDatosExceptoConfirmadas() {
        // Obtener todas las cargas de la base de datos
        List<Carga> cargas = database.obtenerTodasLasCargasExceptoConfirmadas();

        // Limpiar la lista actual de cargas y agregar las cargas recuperadas
        listaCargas.clear();
        listaCargas.addAll(cargas);

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
    }

    // Método para crear el menú contextual para editar o eliminar una carga
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
    }

    // Método para manejar las acciones de los elementos del menú contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Carga cargaSeleccionada = listaCargas.get(info.position);

        switch (item.getItemId()) {
            case R.id.editar:
                // Aquí puedes abrir la actividad de edición de carga
                // Puedes pasar la carga seleccionada como extra en el intent
                Intent intent = new Intent(gestionCargas.this, ActividadEdicionCarga.class);
                intent.putExtra("cargaId", cargaSeleccionada.getId());
                startActivity(intent);
                return true;

            case R.id.conducir:
                //Database database2 = new Database(this);
              //  database2.actualizarEstadoCarga(cargaSeleccionada.getId(), "En Proceso");

                Intent intent2 = new Intent(gestionCargas.this, Navegacion.class);

                // Agregar los datos de la carga seleccionada como extras al intent
                intent2.putExtra("cargaId", cargaSeleccionada.getId());
                intent2.putExtra("nombreDeCarga", cargaSeleccionada.getNombreDeCarga());
                intent2.putExtra("peso", cargaSeleccionada.getPesoEnToneladas());
                intent2.putExtra("ciudadOrigen", cargaSeleccionada.getCiudadDeOrigen());
                intent2.putExtra("ciudadDestino", cargaSeleccionada.getCiudadDeDestino());
                intent2.putExtra("estado", cargaSeleccionada.getEstadoDeCarga());
                intent2.putExtra("creadorCarga", cargaSeleccionada.getCreadorDeCarga());

                // Iniciar la actividad Navegacion
                startActivity(intent2);
                return true;

            case R.id.estadodelviaje:
                //Database database2 = new Database(this);
                //  database2.actualizarEstadoCarga(cargaSeleccionada.getId(), "En Proceso");

                Intent intent3 = new Intent(gestionCargas.this, NavegacionConOdometro.class);

                // Agregar los datos de la carga seleccionada como extras al intent
                intent3.putExtra("cargaId", cargaSeleccionada.getId());
                intent3.putExtra("nombreDeCarga", cargaSeleccionada.getNombreDeCarga());
                intent3.putExtra("peso", cargaSeleccionada.getPesoEnToneladas());
                intent3.putExtra("ciudadOrigen", cargaSeleccionada.getCiudadDeOrigen());
                intent3.putExtra("ciudadDestino", cargaSeleccionada.getCiudadDeDestino());
                intent3.putExtra("estado", cargaSeleccionada.getEstadoDeCarga());
                intent3.putExtra("creadorCarga", cargaSeleccionada.getCreadorDeCarga());

                // Iniciar la actividad Navegacion
                startActivity(intent3);
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
                                cargarCargasDesdeBaseDeDatosExceptoConfirmadas();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
                return true;

            case R.id.actualizar:
                cargarCargasDesdeBaseDeDatosExceptoConfirmadas();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}

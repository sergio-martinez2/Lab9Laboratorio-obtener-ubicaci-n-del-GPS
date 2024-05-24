package com.example.login_php;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.login_php.objetos.Carga;
import com.example.login_php.objetos.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "appcarga"; // el nombre de nuestra base de datos
    private static final int DB_VERSION = 1; // la versión de la base de datos

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USUARIOS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NOMBRE TEXT, "
                + "EMAIL TEXT, "
                + "PASSWORD TEXT, "
                + "TIPO_USUARIO TEXT);");

        db.execSQL("CREATE TABLE CARGAS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NOMBRE_DE_CARGA TEXT, "
                + "PESO INTEGER, "
                + "CIUDAD_ORIGEN TEXT, "
                + "CIUDAD_DESTINO TEXT, "
                + "ESTADO_DE_CARGA TEXT, "
                + "CREADOR_DE_CARGA TEXT);");


        db.execSQL("CREATE TABLE VEHICULOS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PLACA TEXT UNIQUE, " // Aquí se define la placa como única
                + "MARCA TEXT, "
                + "COLOR TEXT, "
                + "MODELO TEXT, "
                + "ESTADO TEXT);");

        // Crear tabla para almacenar los ratings y comentarios de los usuarios
        db.execSQL("CREATE TABLE RATINGS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "EMAIL TEXT UNIQUE, " // Email como clave única para la relación con la tabla de usuarios
                + "RATING REAL, "
                + "COMMENT TEXT);");
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes manejar la actualización de la versión de la base de datos si es necesario
    }

    public void insertUser(SQLiteDatabase db, String nombre, String email, String password, String tipoUsuario) {
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nombre);
        values.put("EMAIL", email);
        values.put("PASSWORD", password);
        values.put("TIPO_USUARIO", tipoUsuario);
        db.insert("USUARIOS", null, values);
        db.close();
    }

    public void insertCarga(String nombreDeCarga, int peso, String ciudadOrigen, String ciudadDestino, String estado, String creadorCarga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE_DE_CARGA", nombreDeCarga);
        values.put("PESO", peso);
        values.put("CIUDAD_ORIGEN", ciudadOrigen);
        values.put("CIUDAD_DESTINO", ciudadDestino);
        values.put("ESTADO_DE_CARGA", estado);
        values.put("CREADOR_DE_CARGA", creadorCarga); // Nuevo campo para el creador de la carga
        db.insert("CARGAS", null, values);
        db.close();
    }
    public void actualizarEstadoCarga(int cargaId, String nuevoEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ESTADO_DE_CARGA", nuevoEstado);
        db.update("CARGAS", values, "ID = ?", new String[]{String.valueOf(cargaId)});
        db.close();
    }


    public void eliminarVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("VEHICULOS", "_id=?", new String[]{String.valueOf(vehiculo.getId())});
        db.close();
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("USUARIOS", // Tabla
                new String[]{"_id", "NOMBRE", "EMAIL", "PASSWORD", "TIPO_USUARIO"}, // Columnas
                "EMAIL=? AND PASSWORD=?", // Clausula WHERE
                new String[]{email, password}, // Valores de la clausula WHERE
                null, null, null);

        boolean userExists = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return userExists;
    }

    public void insertVehiculo(String marca, String modelo, String placa, String color, String estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MARCA", marca);
        values.put("MODELO", modelo);
        values.put("PLACA", placa);
        values.put("COLOR", color);
        values.put("ESTADO", estado);
        db.insert("VEHICULOS", null, values);
        db.close();
    }




    public List<Vehiculo> obtenerTodosLosVehiculos() {
        List<Vehiculo> listaVehiculos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("VEHICULOS", // Tabla de vehículos
                null, // Todas las columnas
                null, // Sin cláusula WHERE
                null, // Sin argumentos para la cláusula WHERE
                null, // Sin agrupamiento
                null, // Sin condición de agrupamiento
                null); // Sin orden

        // Iterar sobre el cursor para obtener los vehículos
        if (cursor.moveToFirst()) {
            do {
                // Obtener los datos de cada vehículo del cursor
                int vehiculoId = cursor.getInt(cursor.getColumnIndex("_id")); // Utiliza "_id" en lugar de "vehiculoId"
                String marca = cursor.getString(cursor.getColumnIndex("MARCA"));
                String modelo = cursor.getString(cursor.getColumnIndex("MODELO"));
                String placa = cursor.getString(cursor.getColumnIndex("PLACA"));
                String color = cursor.getString(cursor.getColumnIndex("COLOR"));
                String estado = cursor.getString(cursor.getColumnIndex("ESTADO"));
                // Crear un nuevo objeto Vehiculo y agregarlo a la lista
                Vehiculo vehiculo = new Vehiculo(vehiculoId, marca, modelo, placa, color, estado);
                listaVehiculos.add(vehiculo);
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor y la base de datos
        cursor.close();


        // Devolver la lista de vehículos
        return listaVehiculos;
    }

    public List<Carga> obtenerTodasLasCargas() {
        List<Carga> listaCargas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CARGAS", // Tabla de cargas
                null, // Todas las columnas
                null, // Sin cláusula WHERE
                null, // Sin argumentos para la cláusula WHERE
                null, // Sin agrupamiento
                null, // Sin condición de agrupamiento
                null); // Sin orden

        // Iterar sobre el cursor para obtener las cargas
        if (cursor.moveToFirst()) {
            do {
                // Obtener los datos de cada carga del cursor
                int cargaId = cursor.getInt(cursor.getColumnIndex("_id")); // Utiliza "_id" en lugar de "cargaId"
                String nombreDeCarga = cursor.getString(cursor.getColumnIndex("NOMBRE_DE_CARGA"));
                int peso = cursor.getInt(cursor.getColumnIndex("PESO"));
                String ciudadOrigen = cursor.getString(cursor.getColumnIndex("CIUDAD_ORIGEN"));
                String ciudadDestino = cursor.getString(cursor.getColumnIndex("CIUDAD_DESTINO"));
                String estado = cursor.getString(cursor.getColumnIndex("ESTADO_DE_CARGA"));
                String creadorDeCarga = cursor.getString(cursor.getColumnIndex("CREADOR_DE_CARGA"));

                // Crear un nuevo objeto Carga y agregarlo a la lista
                Carga carga = new Carga(cargaId, nombreDeCarga, peso, ciudadOrigen, ciudadDestino, estado, creadorDeCarga);
                listaCargas.add(carga);
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        // Devolver la lista de cargas
        return listaCargas;
    }

    public List<Carga> obtenerTodasLasCargasExceptoConfirmadas() {
        List<Carga> listaCargas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {"Confirmada"};
        Cursor cursor = db.query("CARGAS", // Tabla de cargas
                null, // Todas las columnas
                "ESTADO_DE_CARGA != ?", // Clausula WHERE
                selectionArgs, // Argumentos para la cláusula WHERE
                null, // Sin agrupamiento
                null, // Sin condición de agrupamiento
                null); // Sin orden

        // Iterar sobre el cursor para obtener las cargas
        if (cursor.moveToFirst()) {
            do {
                // Obtener los datos de cada carga del cursor
                int cargaId = cursor.getInt(cursor.getColumnIndex("_id")); // Utiliza "_id" en lugar de "cargaId"
                String nombreDeCarga = cursor.getString(cursor.getColumnIndex("NOMBRE_DE_CARGA"));
                int peso = cursor.getInt(cursor.getColumnIndex("PESO"));
                String ciudadOrigen = cursor.getString(cursor.getColumnIndex("CIUDAD_ORIGEN"));
                String ciudadDestino = cursor.getString(cursor.getColumnIndex("CIUDAD_DESTINO"));
                String estado = cursor.getString(cursor.getColumnIndex("ESTADO_DE_CARGA"));
                String creadorDeCarga = cursor.getString(cursor.getColumnIndex("CREADOR_DE_CARGA"));

                // Crear un nuevo objeto Carga y agregarlo a la lista
                Carga carga = new Carga(cargaId, nombreDeCarga, peso, ciudadOrigen, ciudadDestino, estado, creadorDeCarga);
                listaCargas.add(carga);
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        // Devolver la lista de cargas
        return listaCargas;
    }


    public List<Carga> obtenerCargasConEstado(String estado) {
        List<Carga> listaCargas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM CARGAS WHERE ESTADO_DE_CARGA = ?";
        Cursor cursor = db.rawQuery(query, new String[]{estado});

        if (cursor.moveToFirst()) {
            do {
                int cargaId = cursor.getInt(cursor.getColumnIndex("_id"));
                String nombreDeCarga = cursor.getString(cursor.getColumnIndex("NOMBRE_DE_CARGA"));
                int peso = cursor.getInt(cursor.getColumnIndex("PESO"));
                String ciudadOrigen = cursor.getString(cursor.getColumnIndex("CIUDAD_ORIGEN"));
                String ciudadDestino = cursor.getString(cursor.getColumnIndex("CIUDAD_DESTINO"));
                String estadoCarga = cursor.getString(cursor.getColumnIndex("ESTADO_DE_CARGA"));
                String creadorDeCarga = cursor.getString(cursor.getColumnIndex("CREADOR_DE_CARGA"));

                Carga carga = new Carga(cargaId, nombreDeCarga, peso, ciudadOrigen, ciudadDestino, estadoCarga, creadorDeCarga);
                listaCargas.add(carga);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaCargas;
    }



    public void eliminarCarga(Carga carga) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CARGAS", "_id=?", new String[]{String.valueOf(carga.getId())});
        db.close();
    }

    public void actualizarCarga(Carga carga) {
        // Obtener una instancia de la base de datos en modo de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los nuevos valores de la carga
        ContentValues values = new ContentValues();
        values.put("NOMBRE_DE_CARGA", carga.getNombreDeCarga());
        values.put("PESO", carga.getPesoEnToneladas());
        values.put("CIUDAD_ORIGEN", carga.getCiudadDeOrigen());
        values.put("CIUDAD_DESTINO", carga.getCiudadDeDestino());
        values.put("ESTADO_DE_CARGA", carga.getEstadoDeCarga());

        // Definir la cláusula WHERE para identificar la carga que se actualizará
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(carga.getId())};

        // Ejecutar el método update de la base de datos para actualizar el registro
        db.update("CARGAS", values, whereClause, whereArgs);

        // Cerrar la conexión de la base de datos
        db.close();
    }



    //VALIDA USUARIO A LA HORA DE REGISTRAR
    public boolean existeUsuario(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("USUARIOS", // Tabla
                new String[]{"_id"}, // Columnas (en este caso solo necesitamos verificar si existe algún registro)
                "EMAIL=?", // Clausula WHERE
                new String[]{email}, // Valores de la clausula WHERE
                null, null, null);

        boolean existeUsuario = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return existeUsuario;
    }

    public void actualizarVehiculo(Vehiculo vehiculo) {
        // Obtener una instancia de la base de datos en modo de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los nuevos valores del vehículo
        ContentValues values = new ContentValues();
        values.put("MARCA", vehiculo.getMarca());
        values.put("MODELO", vehiculo.getModelo());
        values.put("PLACA", vehiculo.getPlaca());
        values.put("COLOR", vehiculo.getColor());

        // Definir la cláusula WHERE para identificar el vehículo que se actualizará
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(vehiculo.getId())};

        // Ejecutar el método update de la base de datos para actualizar el registro
        db.update("VEHICULOS", values, whereClause, whereArgs);

        // Cerrar la conexión de la base de datos
        db.close();
    }

    public void actualizarEstadoVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ESTADO", vehiculo.getEstado()); // Asegúrate de usar "ESTADO" en mayúsculas

        // Actualizar el estado del vehículo en la base de datos
        db.update("VEHICULOS", values, "_id = ?", new String[]{String.valueOf(vehiculo.getId())});
        db.close();
    }

    public String getUserType(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT TIPO_USUARIO FROM USUARIOS WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        String userType = null;
        if (cursor.moveToFirst()) {
            userType = cursor.getString(cursor.getColumnIndex("TIPO_USUARIO"));
        }

        cursor.close();
        db.close();

        return userType;
    }

    public float getUserRating(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT RATING FROM Ratings WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        float userRating = 0.0f; // Valor predeterminado si no se encuentra la calificación
        if (cursor.moveToFirst()) {
            userRating = cursor.getFloat(cursor.getColumnIndex("RATING"));
        }

        cursor.close();
        db.close();

        return userRating;
    }

    public String getUserComment(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COMMENT FROM Ratings WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        String userComment = ""; // Valor predeterminado si no se encuentra ningún comentario
        if (cursor.moveToFirst()) {
            userComment = cursor.getString(cursor.getColumnIndex("COMMENT"));
        }

        cursor.close();
        db.close();

        return userComment;
    }


    public List<Vehiculo> obtenerVehiculosConEstado(String estado) {
        List<Vehiculo> listaVehiculos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("VEHICULOS",
                null,
                "ESTADO = ?",
                new String[]{estado},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int vehiculoId = cursor.getInt(cursor.getColumnIndex("_id"));
                String marca = cursor.getString(cursor.getColumnIndex("MARCA"));
                String modelo = cursor.getString(cursor.getColumnIndex("MODELO"));
                String placa = cursor.getString(cursor.getColumnIndex("PLACA"));
                String color = cursor.getString(cursor.getColumnIndex("COLOR"));
                String estadoVehiculo = cursor.getString(cursor.getColumnIndex("ESTADO"));

                Vehiculo vehiculo = new Vehiculo(vehiculoId, marca, modelo, placa, color, estadoVehiculo);
                listaVehiculos.add(vehiculo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaVehiculos;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT NOMBRE FROM USUARIOS WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        String userName = null;
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex("NOMBRE"));
        }

        cursor.close();
        db.close();

        return userName;
    }


}

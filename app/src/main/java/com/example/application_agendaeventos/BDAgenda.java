package com.example.application_agendaeventos;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDAgenda extends SQLiteOpenHelper {
    public static final String DB_NAME = "agenda.db";
    public static final int DB_VERSION = 1;

    public BDAgenda(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE estados (" +
                        "id_estado INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre_estado TEXT NOT NULL UNIQUE)"
        );

        db.execSQL(
                "CREATE TABLE tareas (" +
                        "id_tarea INTEGER PRIMARY KEY," + // se ingresa manualmente en tu UI
                        "fecha TEXT NOT NULL," +          // formato yyyy-MM-dd
                        "titulo TEXT NOT NULL," +
                        "descripcion TEXT," +
                        "lugar TEXT," +
                        "id_estado INTEGER NOT NULL," +
                        "FOREIGN KEY(id_estado) REFERENCES estados(id_estado))"
        );

        // Estados iniciales
        db.execSQL("INSERT INTO estados (nombre_estado) VALUES ('Pendiente'), ('En ejecución'), ('Realizado')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS tareas");
        db.execSQL("DROP TABLE IF EXISTS estados");
        onCreate(db);
    }
}

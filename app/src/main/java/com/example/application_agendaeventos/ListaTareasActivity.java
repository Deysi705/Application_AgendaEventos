package com.example.application_agendaeventos;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListaTareasActivity extends AppCompatActivity {
    RecyclerView rv;
    BDAgenda dbAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_tareas);
        rv = findViewById(R.id.recyclerTareas);
        rv.setLayoutManager(new LinearLayoutManager(this));
        dbAgenda = new BDAgenda(this);
        cargarTareas();
    }

    private void cargarTareas() {
        SQLiteDatabase db = dbAgenda.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT t.id_tarea, t.fecha, t.titulo, t.descripcion, t.lugar, e.nombre_estado " +
                        "FROM tareas t JOIN estados e ON t.id_estado = e.id_estado " +
                        "ORDER BY t.fecha DESC", null);

        ArrayList<Tarea> items = new ArrayList<>();
        while (c.moveToNext()){
            Tarea t = new Tarea();
            t.id_tarea = c.getInt(0);
            t.fecha = c.getString(1);
            t.titulo = c.getString(2);
            t.descripcion = c.getString(3);
            t.lugar = c.getString(4);
            t.nombre_estado = c.getString(5);
            items.add(t);
        }
        c.close(); db.close();

        rv.setAdapter(new TareaAdapter(items, item -> {
            // acción al tocar: opcional
        }));
    }
}

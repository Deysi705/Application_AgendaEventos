package com.example.application_agendaeventos;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EstadosActivity extends AppCompatActivity {
    Spinner spEstados;
    Button btnFiltrar;
    RecyclerView rv;
    BDAgenda dbAgenda;
    ArrayList<Integer> estadoIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estados);

        spEstados = findViewById(R.id.spEstadosFiltro);
        btnFiltrar = findViewById(R.id.cmdFiltrar);
        rv = findViewById(R.id.recyclerFiltro);
        rv.setLayoutManager(new LinearLayoutManager(this));
        dbAgenda = new BDAgenda(this);

        cargarEstados();

        btnFiltrar.setOnClickListener(v -> filtrar());
    }

    private void cargarEstados(){
        SQLiteDatabase db = dbAgenda.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_estado, nombre_estado FROM estados ORDER BY id_estado", null);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoIds.clear();
        while (c.moveToNext()){
            estadoIds.add(c.getInt(0));
            adapter.add(c.getString(1));
        }
        c.close(); db.close();
        spEstados.setAdapter(adapter);
    }

    private void filtrar(){
        int pos = spEstados.getSelectedItemPosition();
        if (pos < 0) return;
        int idEstado = estadoIds.get(pos);

        SQLiteDatabase db = dbAgenda.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT t.id_tarea, t.fecha, t.titulo, t.descripcion, t.lugar, e.nombre_estado " +
                        "FROM tareas t JOIN estados e ON t.id_estado = e.id_estado " +
                        "WHERE t.id_estado = ? ORDER BY t.fecha DESC", new String[]{ String.valueOf(idEstado) });

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

        rv.setAdapter(new TareaAdapter(items, item -> {}));
    }
}

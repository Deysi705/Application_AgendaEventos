package com.example.application_agendaeventos;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.ContentValues;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Calendar;

public class RegistrarTareaActivity extends AppCompatActivity {
    EditText txtIdTarea, txtTitulo, txtDescripcion, txtLugar;
    Spinner spEstado;
    Button btnRegistrar, btnCambiar, btnLista, btnEstados;
    DatePicker dateFecha;
    BDAgenda dbAgenda;
    ArrayList<Integer> estadoIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sb = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom);
            return insets;
        });

        dbAgenda = new BDAgenda(this);

        txtIdTarea = findViewById(R.id.txtIdTarea);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtLugar = findViewById(R.id.txtLugar);
        spEstado = findViewById(R.id.spEstado);
        dateFecha = findViewById(R.id.dateFecha);
        btnRegistrar = findViewById(R.id.cmdRegistrar);
        btnCambiar = findViewById(R.id.cmdCambiar);
        btnLista = findViewById(R.id.cmdLista);
        btnEstados = findViewById(R.id.cmdEstados);

        cargarEstadosSpinner();

        btnRegistrar.setOnClickListener(v -> guardarTarea());
        btnCambiar.setOnClickListener(v -> cambiarEstadoPorId());
        btnLista.setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, ListaTareasActivity.class));
        });
        btnEstados.setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, EstadosActivity.class));
        });

        // Opcional: DatePickerDialog al tocar la fecha
        dateFecha.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {});
    }

    private String obtenerFechaISO() {
        int y = dateFecha.getYear();
        int m = dateFecha.getMonth() + 1;
        int d = dateFecha.getDayOfMonth();
        return String.format("%04d-%02d-%02d", y, m, d);
    }

    private void cargarEstadosSpinner() {
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
        spEstado.setAdapter(adapter);
    }

    private void guardarTarea() {
        String sid = txtIdTarea.getText().toString().trim();
        if (sid.isEmpty()) {
            Toast.makeText(this, "Ingresa el id de la tarea", Toast.LENGTH_SHORT).show();
            return;
        }
        int idTarea;
        try { idTarea = Integer.parseInt(sid); }
        catch (NumberFormatException ex) {
            Toast.makeText(this, "El id debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = obtenerFechaISO();
        String titulo = txtTitulo.getText().toString().trim();
        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        String descripcion = txtDescripcion.getText().toString().trim();
        String lugar = txtLugar.getText().toString().trim();
        int pos = spEstado.getSelectedItemPosition();
        if (pos < 0) {
            Toast.makeText(this, "Selecciona un estado", Toast.LENGTH_SHORT).show();
            return;
        }
        int idEstado = estadoIds.get(pos);

        SQLiteDatabase db = dbAgenda.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_tarea", idTarea);
        cv.put("fecha", fecha);
        cv.put("titulo", titulo);
        cv.put("descripcion", descripcion);
        cv.put("lugar", lugar);
        cv.put("id_estado", idEstado);

        long res = db.insert("tareas", null, cv);
        db.close();

        if (res != -1) {
            Toast.makeText(this, "Tarea registrada", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al registrar (id duplicado?)", Toast.LENGTH_SHORT).show();
        }
    }

    private void cambiarEstadoPorId() {
        String sid = txtIdTarea.getText().toString().trim();
        if (sid.isEmpty()) {
            Toast.makeText(this, "Ingresa el id para cambiar estado", Toast.LENGTH_SHORT).show();
            return;
        }
        int idTarea;
        try { idTarea = Integer.parseInt(sid); }
        catch (NumberFormatException ex) {
            Toast.makeText(this, "El id debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }
        int pos = spEstado.getSelectedItemPosition();
        if (pos < 0) {
            Toast.makeText(this, "Selecciona un estado", Toast.LENGTH_SHORT).show();
            return;
        }
        int idEstado = estadoIds.get(pos);

        SQLiteDatabase db = dbAgenda.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_estado", idEstado);
        int filas = db.update("tareas", cv, "id_tarea = ?", new String[]{ String.valueOf(idTarea) });
        db.close();

        if (filas > 0) {
            Toast.makeText(this, "Estado actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe una tarea con ese id", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        txtIdTarea.setText("");
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtLugar.setText("");
        // fecha se mantiene
        spEstado.setSelection(0);
    }
}

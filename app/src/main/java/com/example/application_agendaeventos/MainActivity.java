package com.example.application_agendaeventos;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sb = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom);
            return insets;
        });
    }

    public void cmdRegistrarTarea_OnClick(View v){
        startActivity(new Intent(this, RegistrarTareaActivity.class));
    }
    public void cmdListaTareas_OnClick(View v){
        startActivity(new Intent(this, ListaTareasActivity.class));
    }
    public void cmdEstados_OnClick(View v){
        startActivity(new Intent(this, EstadosActivity.class));
    }
}


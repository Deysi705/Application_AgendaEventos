package com.example.application_agendaeventos;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.VH> {
    public interface OnItemClick {
        void onClick(Tarea item);
    }

    private List<Tarea> data;
    private OnItemClick listener;

    public TareaAdapter(List<Tarea> data, OnItemClick listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        Tarea t = data.get(pos);
        h.txtTitulo.setText(t.titulo);
        h.txtPersona.setText(t.lugar); // Puedes usar "lugar" como segundo campo visible
        h.txtEstado.setText(t.nombre_estado);
        h.txtFecha.setText(t.fecha);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtPersona, txtEstado, txtFecha;

        VH(View v) {
            super(v);
            txtTitulo = v.findViewById(R.id.txtItemTitulos);
            txtPersona = v.findViewById(R.id.txtItemPersonas);
            txtEstado = v.findViewById(R.id.txtItemEstados);
            txtFecha = v.findViewById(R.id.txtItemFecha);
        }
    }
}


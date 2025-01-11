package controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;
import model.Pedido;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> listaPedidos;

    // Constructor
    public PedidoAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el layout del item de cada pedido
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        // Configurar los datos del pedido en el viewHolder
        Pedido pedido = listaPedidos.get(position);
        holder.txtCliente.setText(pedido.getCliente());
        holder.txtFecha.setText(pedido.getFecha());
        holder.txtTotal.setText("$" + String.format("%.2f", pedido.getTotalPedido()));
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    // Clase interna para mantener las vistas de cada item
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView txtCliente, txtFecha, txtTotal;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            txtCliente = itemView.findViewById(R.id.txtCliente);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}

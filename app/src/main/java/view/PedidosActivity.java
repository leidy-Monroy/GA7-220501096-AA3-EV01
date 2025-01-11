package view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import controller.PedidoAdapter;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import model.Pedido;
import utils.DatabaseHelper;

public class PedidosActivity extends AppCompatActivity {

    RecyclerView recyclerViewPedidos;
    FloatingActionButton fabAgregarPedido;
    PedidoAdapter adapter;
    List<Pedido> listaPedidos;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        fabAgregarPedido = findViewById(R.id.fabAgregarPedido);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar la lista de pedidos
        listaPedidos = new ArrayList<>();

        // Configurar RecyclerView
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PedidoAdapter(listaPedidos);
        recyclerViewPedidos.setAdapter(adapter);

        // Cargar pedidos del servidor
        DatabaseHelper.cargarPedidos(this, listaPedidos, adapter, progressBar);

        // Acción del FAB
        fabAgregarPedido.setOnClickListener(v -> {
            Intent intent = new Intent(PedidosActivity.this, RegistrarPedidoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista de pedidos al reanudar la actividad
        DatabaseHelper.cargarPedidos(this, listaPedidos, adapter, progressBar);
    }
}

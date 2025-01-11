package view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    CardView btnClientes, btnPedidos, btnProductos, btnDetallePedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("App de Floristeria");
        }

        // Inicializar botones
        btnClientes = findViewById(R.id.btnClientes);
        btnPedidos = findViewById(R.id.btnPedidos);
        btnProductos = findViewById(R.id.btnProductos);
        btnDetallePedido = findViewById(R.id.btnDetallePedido);

        // Configurar listeners para los botones
        btnClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInsertarClienteActivity();
            }
        });

        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPedidosActivity();
            }
        });

        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterProductActivity();
            }
        });

        btnDetallePedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity("Reporte");
            }
        });
    }

    // Método para abrir la actividad de insertar cliente
    private void openInsertarClienteActivity() {
        Intent intent = new Intent(MainActivity.this, InsertarClienteActivity.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de pedidos
    private void openPedidosActivity() {
        Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de registro de producto
    private void openRegisterProductActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterProductActivity.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de detalles (Reporte)
    private void openDetailActivity(String type) {
        Intent intent = new Intent(MainActivity.this, OrderDetailsActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}

package view;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.R;
import java.util.Calendar;
import utils.DatabaseHelper;

public class RegistrarPedidoActivity extends AppCompatActivity {

    private EditText etIdCliente, etFechaPedido, etTotalPedido, etEstadoPedido;
    private Button btnRegistrarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pedido);

        // Vincular elementos de la interfaz
        etIdCliente = findViewById(R.id.et_id_cliente);
        etFechaPedido = findViewById(R.id.et_fecha_pedido);
        etTotalPedido = findViewById(R.id.et_total_pedido);
        etEstadoPedido = findViewById(R.id.et_estado_pedido);
        btnRegistrarPedido = findViewById(R.id.btn_registrar_pedido);

        // Configurar selector de fecha
        etFechaPedido.setOnClickListener(v -> mostrarSelectorFecha());

        // Acción del botón
        btnRegistrarPedido.setOnClickListener(v -> registrarPedido());
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String fechaSeleccionada = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            etFechaPedido.setText(fechaSeleccionada);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void registrarPedido() {
        String idCliente = etIdCliente.getText().toString();
        String fechaPedido = etFechaPedido.getText().toString();
        String totalPedido = etTotalPedido.getText().toString();
        String estadoPedido = etEstadoPedido.getText().toString();

        if (idCliente.isEmpty() || fechaPedido.isEmpty() || totalPedido.isEmpty() || estadoPedido.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamar al método en DatabaseHelper
        DatabaseHelper.registrarPedido(this, idCliente, fechaPedido, totalPedido, estadoPedido, success -> {
            if (success) {
                Toast.makeText(this, "Pedido registrado con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();

                // Navegar a PedidosActivity
                startActivity(new Intent(this, PedidosActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error al registrar el pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etIdCliente.setText("");
        etFechaPedido.setText("");
        etTotalPedido.setText("");
        etEstadoPedido.setText("");
    }
}

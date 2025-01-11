package view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import com.example.myapplication.R;
import utils.DatabaseHelper;


public class InsertarClienteActivity extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtDireccion, edtTelefono, edtCorreo;
    Button btnGuardarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_cliente);

        // Inicializar los campos y el botón
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtCorreo = findViewById(R.id.edtCorreo);
        btnGuardarCliente = findViewById(R.id.btnGuardarCliente);

        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombre.getText().toString();
                String apellido = edtApellido.getText().toString();
                String direccion = edtDireccion.getText().toString();
                String telefono = edtTelefono.getText().toString();
                String correo = edtCorreo.getText().toString();

                // Verificar si los campos no están vacíos
                if (!nombre.isEmpty() && !apellido.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty() && !correo.isEmpty()) {
                    // Crear un nuevo Thread para la petición HTTP
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Llamar al método de DatabaseHelper para insertar el cliente
                            boolean success = DatabaseHelper.insertarCliente(nombre, apellido, direccion, telefono, correo);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        Toast.makeText(InsertarClienteActivity.this, "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
                                        limpiarCampos(); // Limpiar los campos después de registrar
                                    } else {
                                        Toast.makeText(InsertarClienteActivity.this, "Error al guardar el cliente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start(); // Iniciar el Thread
                } else {
                    // Mostrar un mensaje si los campos están vacíos
                    Toast.makeText(InsertarClienteActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para limpiar los campos
    private void limpiarCampos() {
        edtNombre.setText("");
        edtApellido.setText("");
        edtDireccion.setText("");
        edtTelefono.setText("");
        edtCorreo.setText("");
    }
}

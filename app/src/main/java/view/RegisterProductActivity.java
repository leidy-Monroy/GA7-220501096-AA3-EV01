package view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import utils.DatabaseHelper;

public class RegisterProductActivity extends AppCompatActivity {

    EditText etProductName, etProductPrice, etProductDescription, etProductStock;
    Button btnSaveProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductStock = findViewById(R.id.etProductStock);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        btnSaveProduct.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String productName = etProductName.getText().toString();
        String productPrice = etProductPrice.getText().toString();
        String productDescription = etProductDescription.getText().toString();
        String productStock = etProductStock.getText().toString();

        // Validar que no haya campos vacíos
        if (productName.isEmpty() || productPrice.isEmpty() || productDescription.isEmpty() || productStock.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar producto con callback para limpiar campos
        DatabaseHelper.registerProduct(this, productName, productPrice, productDescription, productStock, this::clearFields);
    }

    // Método para limpiar los campos
    private void clearFields() {
        etProductName.setText("");
        etProductPrice.setText("");
        etProductDescription.setText("");
        etProductStock.setText("");
        Toast.makeText(this, "registro exitoso", Toast.LENGTH_SHORT).show();
    }
}

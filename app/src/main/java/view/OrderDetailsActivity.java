package view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import controller.ProductAdapter;
import model.Product;
import utils.DatabaseHelper;
import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    public ListView lvProductList;
    public TextView tvOrderTotal;
    public ArrayList<Product> productList;
    public ProductAdapter adapter;
    public ProgressBar progressBar; // Referencia al ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        lvProductList = findViewById(R.id.lvProducts);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        progressBar = findViewById(R.id.progressBar); // Enlaza el ProgressBar

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        lvProductList.setAdapter(adapter);

        // Llamada inicial para obtener todos los productos
        DatabaseHelper.fetchProducts(this);

        // Configura el evento de clic en los elementos de la lista
        lvProductList.setOnItemClickListener((parent, view, position, id) -> {
            Product selectedProduct = productList.get(position);

            Button btnUpdate = view.findViewById(R.id.btnUpdate);
            Button btnDelete = view.findViewById(R.id.btnDelete);

            // Acciones de clic para el botón "Actualizar"
            btnUpdate.setOnClickListener(v -> updateProduct(selectedProduct));

            // Acciones de clic para el botón "Eliminar"
            btnDelete.setOnClickListener(v -> deleteProduct(selectedProduct));
        });
    }

    private void updateProduct(Product product) {
        // Lógica de actualización, puedes modificar cualquier campo del producto
        product.setNombreProducto("Nuevo nombre");

        // Convertir el precio y el stock a String antes de pasarlos al método
        String precio = String.valueOf(product.getPrecioProducto());
        String stock = String.valueOf(product.getStockProducto());

        // Llamada al método updateProduct con los valores correctos
        DatabaseHelper.updateProduct(this,
                String.valueOf(product.getIdProducto()),
                product.getNombreProducto(),
                precio,
                product.getDescripcionProducto(),
                stock,
                new Runnable() {
                    @Override
                    public void run() {
                        // Refrescar la lista después de la actualización
                        refreshProductList();
                    }
                });
    }

    private void deleteProduct(Product product) {
        // Lógica para eliminar el producto
        DatabaseHelper.deleteProduct(this, String.valueOf(product.getIdProducto()), new Runnable() {
            @Override
            public void run() {
                // Refrescar la lista después de la eliminación
                refreshProductList();
            }
        });
    }


    // Método para refrescar la lista de productos
    private void refreshProductList() {
        // Refresca los productos desde la base de datos
        DatabaseHelper.fetchProducts(this);

        // Aquí actualizamos el total después de refrescar la lista
        updateTotal();
    }


    private void updateTotal() {
        double totalOrder = 0.0;
        // Suma los precios de todos los productos en la lista
        for (Product product : productList) {
            totalOrder += product.getPrecioProducto() * product.getStockProducto();
        }
        // Actualiza el total en la vista
        tvOrderTotal.setText("Total: $" + String.format("%.2f", totalOrder));
    }

}

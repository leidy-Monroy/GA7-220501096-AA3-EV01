package controller;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.R;
import java.util.List;
import model.Product;
import utils.DatabaseHelper;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> products;


    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        Product product = products.get(position);

        // Encontramos las vistas
        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvProductDescription = convertView.findViewById(R.id.tvProductDescription);
        TextView tvProductPrice = convertView.findViewById(R.id.tvProductPrice);
        TextView tvProductStock = convertView.findViewById(R.id.tvProductStock);
        ImageButton btnUpdate = convertView.findViewById(R.id.btnUpdate);
        ImageButton  btnDelete = convertView.findViewById(R.id.btnDelete);

        // Asignamos los valores
        tvProductName.setText(product.getNombreProducto());
        tvProductDescription.setText(product.getDescripcionProducto());
        tvProductPrice.setText(String.format("$%.2f", product.getPrecioProducto()));
        tvProductStock.setText("Stock: " + product.getStockProducto());

        // Configurar acción del botón Actualizar
        btnUpdate.setOnClickListener(v -> showUpdateDialog(product, position));

        // Configurar acción del botón Eliminar
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar este producto?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        // Eliminar de la base de datos
                        DatabaseHelper.deleteProduct(context, String.valueOf(product.getIdProducto()), new Runnable() {
                            @Override
                            public void run() {
                                // Eliminar de la lista local y actualizar la vista
                                products.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return convertView;
    }

    private void showUpdateDialog(Product product, int position) {
        // Crear el diseño del diálogo
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_product, null);

        TextView etName = dialogView.findViewById(R.id.etProductName);
        TextView etDescription = dialogView.findViewById(R.id.etProductDescription);
        TextView etPrice = dialogView.findViewById(R.id.etProductPrice);
        TextView etStock = dialogView.findViewById(R.id.etProductStock);

        // Prellenar los datos actuales
        etName.setText(product.getNombreProducto());
        etDescription.setText(product.getDescripcionProducto());
        etPrice.setText(String.valueOf(product.getPrecioProducto()));
        etStock.setText(String.valueOf(product.getStockProducto()));

        // Mostrar el diálogo
        new AlertDialog.Builder(context)
                .setTitle("Actualizar producto")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Actualizar los datos del producto
                    product.setNombreProducto(etName.getText().toString());
                    product.setDescripcionProducto(etDescription.getText().toString());
                    product.setPrecioProducto(Double.parseDouble(etPrice.getText().toString()));
                    product.setStockProducto(Integer.parseInt(etStock.getText().toString()));

                    // Llamada a la base de datos para actualizar el producto
                    DatabaseHelper.updateProduct(context,
                            String.valueOf(product.getIdProducto()),  // Convertir el idProducto a String
                            product.getNombreProducto(),
                            String.valueOf(product.getPrecioProducto()),
                            product.getDescripcionProducto(),
                            String.valueOf(product.getStockProducto()),
                            new Runnable() {
                                @Override
                                public void run() {
                                    // Actualizar la vista
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}

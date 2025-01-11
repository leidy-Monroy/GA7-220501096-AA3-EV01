package utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import model.Pedido;
import controller.PedidoAdapter;
import model.Product;
import view.OrderDetailsActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    private static final String BASE_URL = "http://192.168.0.14/apis_floristeria/";

    public interface RequestCallback {
        void onComplete(boolean success);
    }

    public static void registrarPedido(Context context, String idCliente, String fechaPedido, String totalPedido, String estadoPedido, RequestCallback callback) {
        String url = BASE_URL + "registrarPedido.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("DatabaseHelper", "Respuesta del servidor: " + response);
                    callback.onComplete(true);
                },
                error -> {
                    Log.e("DatabaseHelper", "Error al registrar pedido: " + error.getMessage());
                    callback.onComplete(false);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_cliente", idCliente);
                params.put("fecha_pedido", fechaPedido);
                params.put("total_pedido", totalPedido);
                params.put("estado_pedido", estadoPedido);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void cargarPedidos(Context context, List<Pedido> listaPedidos, PedidoAdapter adapter, ProgressBar progressBar) {
        String url = BASE_URL + "listar_pedidos.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Mostrar el ProgressBar antes de empezar la solicitud
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    listaPedidos.clear();
                    double total = 0;

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject pedidoJson = response.getJSONObject(i);
                            int idPedido = pedidoJson.getInt("id_pedido");
                            String cliente = pedidoJson.getString("id_cliente");
                            String fecha = pedidoJson.getString("fecha_pedido");
                            double totalPedido = pedidoJson.getDouble("total_pedido");

                            listaPedidos.add(new Pedido(idPedido, cliente, fecha, totalPedido));
                            total += totalPedido;
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error al procesar datos.", Toast.LENGTH_SHORT).show();
                    } finally {
                        // Ocultar el ProgressBar una vez que la solicitud haya terminado
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    Toast.makeText(context, "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseHelper", "Error: " + error.toString());
                    // Ocultar el ProgressBar si hay un error
                    progressBar.setVisibility(View.GONE);
                });

        requestQueue.add(request);
    }

    public static boolean insertarCliente(String nombre, String apellido, String direccion, String telefono, String correo) {
        try {
            String url = BASE_URL + "insertar_cliente.php";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "nombre=" + nombre + "&apellido=" + apellido + "&direccion=" + direccion +
                    "&telefono=" + telefono + "&correo=" + correo;

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData.getBytes());
            outputStream.flush();

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void fetchProducts(OrderDetailsActivity activity) {
        // Muestra el ProgressBar antes de iniciar la carga
        activity.progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String url = BASE_URL + "order_details.php?action=fetch";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    activity.productList.clear();
                    double totalOrder = 0.0;

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject product = response.getJSONObject(i);

                            int idProducto = product.getInt("id_producto");
                            String nombreProducto = product.getString("nombre_producto");
                            String descripcionProducto = product.getString("descripcion_producto");
                            double precioProducto = product.getDouble("precio_producto");
                            int stockProducto = product.getInt("stock_producto");

                            Product productObj = new Product(idProducto, nombreProducto, descripcionProducto, precioProducto, stockProducto);
                            activity.productList.add(productObj);
                            totalOrder += precioProducto;
                        }

                        activity.adapter.notifyDataSetChanged();
                        activity.tvOrderTotal.setText("Total: $" + String.format("%.2f", totalOrder));
                    } catch (JSONException e) {
                        Toast.makeText(activity, "Error al procesar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        // Oculta el ProgressBar una vez que los datos se hayan cargado
                        activity.progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    Toast.makeText(activity, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OrderDetails", "Error: " + error.toString());
                    // Oculta el ProgressBar incluso si ocurre un error
                    activity.progressBar.setVisibility(View.GONE);
                });

        requestQueue.add(jsonArrayRequest);
    }

    // Método para registrar producto
    public static void registerProduct(Context context, String name, String price, String description, String stock, Runnable onSuccess) {
        String url = BASE_URL + "register_product.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Manejar respuesta del servidor
                    Toast.makeText(context, "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) {
                        onSuccess.run(); // Ejecutar el callback para limpiar campos
                    }
                },
                error -> {
                    // Manejar errores
                    Toast.makeText(context, "Error al registrar producto: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseHelper", "Error al registrar producto: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre_producto", name);
                params.put("descripcion_producto", description);
                params.put("precio_producto", price);
                params.put("stock_producto", stock);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Método para actualizar el producto
    public static void updateProduct(Context context, String id, String name, String price, String description, String stock, Runnable onSuccess) {
        String url = BASE_URL + "actualizar_producto.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Manejar respuesta del servidor
                    Toast.makeText(context, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) {
                        onSuccess.run(); // Ejecutar el callback para cualquier acción posterior
                    }
                },
                error -> {
                    // Manejar errores
                    Toast.makeText(context, "Error al actualizar producto: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseHelper", "Error al actualizar producto: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_producto", id);
                params.put("nombre_producto", name);
                params.put("descripcion_producto", description);
                params.put("precio_producto", price);
                params.put("stock_producto", stock);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void deleteProduct(Context context, String productId, Runnable onSuccess) {
        String url = BASE_URL + "eliminar_producto.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Debugging: Log para la respuesta del servidor
                    Log.d("Response", response);

                    // Manejar respuesta del servidor
                    Toast.makeText(context, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) {
                        onSuccess.run(); // Ejecutar el callback para realizar una acción después
                    }
                },
                error -> {
                    // Manejar errores
                    Toast.makeText(context, "Error al eliminar producto: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseHelper", "Error al eliminar producto: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_producto", productId);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}

package com.example.algroup

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject
import java.util.Locale

class carritoDeCompras : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var productosUsuario: ArrayList<String>
    private lateinit var productosLegibles: ArrayList<String>
    private lateinit var preciosProductos: MutableMap<String, Pair<Double, Int>> // Cambio en el tipo de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito_de_compras)

        val atras = findViewById<ImageView>(R.id.back1)
        val listView = findViewById<ListView>(R.id.listViewProductos)
        val monto = findViewById<TextView>(R.id.monto)
        val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("nombre", "")
        productosUsuario = obtenerProductosUsuario(nombreUsuario.toString())

        // Modificar productosLegibles para convertir JSON a cadenas legibles
        productosLegibles = ArrayList()
        preciosProductos = HashMap()

        atras.setOnClickListener {
            val atrasPass= Intent(this, menu::class.java)
            startActivity(atrasPass)
        }

        for (productoJSON in productosUsuario) {
            val nombreProducto = obtenerNombreProducto(productoJSON)
            val (precioProducto, cantidadProducto) = obtenerPrecioProducto(productoJSON)
            productosLegibles.add("$nombreProducto    /    $precioProducto x $cantidadProducto") // Cambio en el formato de muestra
            preciosProductos[nombreProducto] = Pair(precioProducto, cantidadProducto) // Cambio en el tipo de datos
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productosLegibles)
        listView.adapter = adapter

        // Configurar el clic en un elemento de la lista para mostrar un cuadro de diálogo
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = productosUsuario[position]
            mostrarDialogo(selectedProduct, nombreUsuario, position, monto)
        }

        // Calcular la suma de los precios
        var sumaPrecios = 0.0
        for ((precioProducto, cantidadProducto) in preciosProductos.values) {
            sumaPrecios += precioProducto * cantidadProducto
        }

        // Formatear la suma con dos decimales
        val montoFormateado = String.format(Locale.US, "%.2f", sumaPrecios)

        // Mostrar la suma formateada en el TextView "monto"
        monto.text = montoFormateado
    }

    private fun obtenerProductosUsuario(nombreUsuario: String): ArrayList<String> {
        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.readableDatabase

        val productos = ArrayList<String>()

        val cursor = db.query(
            "productos",
            arrayOf("producto"), // Solo necesitas obtener el producto
            "usuario = ?",
            arrayOf(nombreUsuario),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val productoJSON = cursor.getString(cursor.run { getColumnIndex("producto") })
            productos.add(productoJSON)
        }

        cursor.close()
        db.close()

        return productos
    }

    private fun obtenerNombreProducto(productoJSON: String): String {
        val jsonObject = JSONObject(productoJSON)
        return jsonObject.optString("Producto", "")
    }

    private fun obtenerPrecioProducto(productoJSON: String): Pair<Double, Int> {
        // Obtén la cantidad desde la base de datos
        val cantidad = obtenerCantidadDesdeBaseDeDatos(productoJSON)

        val jsonObject = JSONObject(productoJSON)
        val precio = jsonObject.optDouble("Precio", 0.0)

        return Pair(precio, cantidad)
    }

    private fun obtenerCantidadDesdeBaseDeDatos(productoJSON: String): Int {
        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "productos",
            arrayOf("cantidad"), // Solo necesitas obtener la cantidad
            "producto = ?",
            arrayOf(productoJSON),
            null,
            null,
            null
        )

        var cantidad = 0 // Valor predeterminado en caso de no encontrar nada
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
        }

        cursor.close()
        db.close()

        return cantidad
    }

    private fun mostrarDialogo(productoJSON: String, user: String?, position: Int, montoTextView: TextView) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.remover, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()
        dialog.show()

        val remover = dialogView.findViewById<TextView>(R.id.eliminar)
        val mantener = dialogView.findViewById<TextView>(R.id.mantener)

        val (precioProducto, cantidadProducto) = obtenerPrecioProducto(productoJSON)

        val mensajeDialogo = "$cantidadProducto x $precioProducto"
        dialog.setMessage(mensajeDialogo)

        remover.setOnClickListener {
            var sumaPrecios = montoTextView.text.toString().toDouble()
            sumaPrecios -= precioProducto * cantidadProducto
            val montoFormateado = String.format(Locale.US, "%.2f", sumaPrecios)
            montoTextView.text = montoFormateado

            val nombreProducto = obtenerNombreProducto(productoJSON)
            productosLegibles.remove("$nombreProducto    /    $precioProducto x $cantidadProducto")
            adapter.notifyDataSetChanged()

            eliminarProductoDeBaseDeDatos(productoJSON, user)

            dialog.dismiss()
        }

        mantener.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun eliminarProductoDeBaseDeDatos(productoJSON: String, user: String?) {
        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        db.beginTransaction()
        try {
            db.delete("productos", "producto = ? AND usuario = ?", arrayOf(productoJSON, user))
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            // Manejar cualquier excepción aquí si es necesario
        } finally {
            db.endTransaction()
        }

        db.close()
    }
}

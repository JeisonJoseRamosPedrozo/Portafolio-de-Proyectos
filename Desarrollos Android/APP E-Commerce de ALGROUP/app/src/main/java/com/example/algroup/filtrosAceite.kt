package com.example.algroup

import android.content.ContentValues
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import org.json.JSONObject

class filtrosAceite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtros_aceite)

        val img1 = findViewById<ImageView>(R.id.imgALO19011)
        val img2 = findViewById<ImageView>(R.id.imgALO10517)
        val img3 = findViewById<ImageView>(R.id.imgALO81691)
        val img4 = findViewById<ImageView>(R.id.imgALO19005)
        val img5 = findViewById<ImageView>(R.id.imgALO8789)
        val img6 = findViewById<ImageView>(R.id.imgALO10506)
        val img7 = findViewById<ImageView>(R.id.imgALO8788)
        val img8 = findViewById<ImageView>(R.id.imgALO8778)
        val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("nombre", "")
        val name = nombreUsuario.toString()

        img1.setOnClickListener {
            producto1(name)
        }
        img2.setOnClickListener {
            producto2(name)
        }
        img3.setOnClickListener {
            producto3(name)
        }
        img4.setOnClickListener {
            producto4(name)
        }
        img5.setOnClickListener {
            producto5(name)
        }
        img6.setOnClickListener {
            producto6(name)
        }
        img7.setOnClickListener {
            producto7(name)
        }
        img8.setOnClickListener {
            producto8(name)
        }
    }

    private fun producto1(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-19011"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 1,05 / DIAMETRO: 110.0 / ALTURA: 188,5 / ROSCA: 1-3/8-16 ONU-2B"
        val descrip2 = "Dimensiones (Pulgada): PESO: 0,04 / DIAMETRO: 4.33 / ALTURA: 7.42 / ROSCA: 0,04"
        val descrip3 = "Dimensiones (Cm): PESO: 0,11 / DIAMETRO: 11.00 / ALTURA: 18,85 / ROSCA: 0,10"
        val price = "20.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type

        - $descrip1

        - $descrip2

        - $descrip3

        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)

            // Cambiar el color del EditText
            quantityEditText.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }

    private fun producto2(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-10517"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 0,00 kilogramos / 0,16 libras/ DIAMETRO INTERIOR: 25,9 / ALTURA: 100.0"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.00/ DIAMETRO INTERIOR: 1.02 /ALTURA: 3.94"
        val descrip3 = "Dimensiones (Cm): PESO: 0.00/ DIAMETRO INTERIOR: 2.59 /ALTURA: 10.00"
        val price = "30.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        - $descrip2
        
        - $descrip3
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


    private fun producto3(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-81691"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 0,13 kilogramos / DIAMETRO INTERIOR: 28,7 /ALTURA: 200.0"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.00/ DIAMETRO INTERIOR: 1.13 /ALTURA: 7,87"
        val descrip3 = "Dimensiones (Cm): PESO: 0,01/ DIAMETRO INTERIOR: 2,87 /ALTURA: 20.00"
        val price = "22.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        - $descrip2
        
        - $descrip3
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


    private fun producto4(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-19005"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 0,50 kilos / DIAMETRO: 94,5 / ALTURA: 137,7 / ROSCA: 1 1/8-16 ONU-2B"
        val descrip2 = "Dimensiones (Pulgada): PESO: 0,02 / DIAMETRO: 3.72 / ALTURA: 5.42 / ROSCA: 0,04"
        val descrip3 = "Dimensiones (Cm): PESO: 0,05 / DIAMETRO: 9.45 / ALTURA: 13.77 / ROSCA: 0,10"
        val price = "25.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        - $descrip2
        
        - $descrip3
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


    private fun producto5(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-8789"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Sin Información de Dimensiones"
        val price = "24.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


    private fun producto6(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-10506"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 0,29 kilogramos / 0,65 libras/ DIAMETRO TOTAL: 108.0 / DIAMETRO INTERIOR: 27,7 /ALTURA: 161,5"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.01/ DIAMETRO TOTAL: 4.25 / DIAMETRO INTERIOR: 1.09 /ALTURA: 6.36"
        val descrip3 = "Dimensiones (Cm): PESO: 0,03/ DIAMETRO TOTAL: 10.80 / DIAMETRO INTERIOR: 2.77 /ALTURA: 16.15"
        val price = "31.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        - $descrip2
        
        - $descrip3
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


    private fun producto7(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-8788"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 0,10 kilogramos / 0,23 libras/ DIAMETRO TOTAL: 77,2 / DIAMETRO INTERIOR: 77,2 /ALTURA: 82.0"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.00/ DIAMETRO TOTAL: 3.04 / DIAMETRO INTERIOR: 3.04 /ALTURA: 3.23"
        val descrip3 = "Dimensiones (Cm): PESO: 0.01/ DIAMETRO TOTAL: 7,72 / DIAMETRO INTERIOR: 7,72 /ALTURA: 8.20"
        val price = "35.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        - $descrip2
        
        - $descrip3
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


    private fun producto8(nombreUsuario: String) {
        val nameProduct = "Filtro de Aceite ALO-8778"
        val type = "Filtro de Tipo Aceite"
        val descrip1 = "Dimensiones (MM): PESO: 0,14 kilogramos / 0,32 libras/ DIAMETRO TOTAL: 67,5 / DIAMETRO INTERIOR: 27.0 /ALTURA: 162.0"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.01/ DIAMETRO TOTAL: 2.66 / DIAMETRO INTERIOR: 1.06 /ALTURA: 6.38"
        val descrip3 = "Dimensiones (Cm): PESO: 0.01/ DIAMETRO TOTAL: 6.75 / DIAMETRO INTERIOR: 2.70 /ALTURA: 16.20"
        val price = "18.99"

        // EditText para ingresar la cantidad
        val quantityEditText = EditText(this)
        quantityEditText.hint = "Cantidad"
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER

        // Concatenar las descripciones en un solo mensaje
        val descripcionCompleta = """
        Tipo: $type
        
        - $descrip1
        
        - $descrip2
        
        - $descrip3
        
        Precio: $price
    """.trimIndent()

        val dbHelper = SQLite(this, "algroup", null, 1)
        val db = dbHelper.writableDatabase

        val productos = JSONObject()
        productos.put("Producto", nameProduct)
        productos.put("Tipo", type)
        productos.put("Descripción", descripcionCompleta)
        productos.put("Precio", price)

        val json_a_String = productos.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Producto: $nameProduct")
        builder.setMessage(descripcionCompleta)

        builder.setView(quantityEditText) // Agregar el EditText al AlertDialog

        builder.setPositiveButton("Añadir al Carrito") { dialog, _ ->
            val cantidad = quantityEditText.text.toString()
            if (cantidad.isNotEmpty()) {
                // Obtener la cantidad ingresada y convertirla a entero
                val cantidadInt = cantidad.toInt()

                // Verificar si el producto ya existe en la base de datos del usuario actual
                val cursor = db.query(
                    "productos",
                    arrayOf("producto", "cantidad"), // Agrega "cantidad" a la consulta
                    "usuario = ? AND producto = ?",
                    arrayOf(nombreUsuario, json_a_String),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // El producto ya existe, mostrar un diálogo de confirmación para reemplazar la cantidad
                    val existingQuantity = cursor.getInt(cursor.run { getColumnIndex("cantidad") })
                    val replaceBuilder = AlertDialog.Builder(this)
                    replaceBuilder.setTitle("Producto ya añadido")
                    replaceBuilder.setMessage("Ya hay $existingQuantity unidades de este producto en el carrito ¿Desea reemplazar la cantidad?")

                    replaceBuilder.setPositiveButton("Sí") { _, _ ->
                        // Eliminar el producto existente
                        db.delete(
                            "productos",
                            "usuario = ? AND producto = ?",
                            arrayOf(nombreUsuario, json_a_String)
                        )

                        // Insertar el nuevo registro con la cantidad actualizada
                        val productoTabla = ContentValues()
                        productoTabla.put("usuario", nombreUsuario)
                        productoTabla.put("producto", json_a_String)
                        productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                        db.insert("productos", null, productoTabla)

                        Toast.makeText(this, "Cantidad del producto reemplazada", Toast.LENGTH_LONG).show()
                    }

                    replaceBuilder.setNegativeButton("No") { _, _ ->
                        // No se reemplaza, simplemente cierra el diálogo
                    }

                    val replaceDialog = replaceBuilder.create()
                    replaceDialog.show()
                } else {
                    // El producto no existe, insertar un nuevo registro
                    val productoTabla = ContentValues()
                    productoTabla.put("usuario", nombreUsuario)
                    productoTabla.put("producto", json_a_String)
                    productoTabla.put("cantidad", cantidadInt) // Agregar la cantidad a la base de datos

                    db.insert("productos", null, productoTabla)

                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Establecer el color de fondo
        val colorFondo = ContextCompat.getColor(this, R.color.mi_color_fondo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(colorFondo))
        // Cambiar el color del texto del AlertDialog
        dialog.setOnShowListener {
            val colorTexto = ContextCompat.getColor(this, android.R.color.holo_purple)
            val colorBoton1 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val colorBoton2 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.setTextColor(colorTexto)

            // Cambiar el color de los botones
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(colorBoton1)
            negativeButton.setTextColor(colorBoton2)
        }

        dialog.show()
    }


}
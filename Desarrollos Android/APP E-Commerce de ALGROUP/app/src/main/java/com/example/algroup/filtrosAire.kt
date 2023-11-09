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

class filtrosAire : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtros_aire)

        val img1 = findViewById<ImageView>(R.id.imgALA17049)
        val img2 = findViewById<ImageView>(R.id.imgALA18139)
        val img3 = findViewById<ImageView>(R.id.imgALA8928)
        val img4 = findViewById<ImageView>(R.id.imgALA18129)
        val img5 = findViewById<ImageView>(R.id.imgALA18126)
        val img6 = findViewById<ImageView>(R.id.imgALA1807)
        val img7 = findViewById<ImageView>(R.id.imgALA18120)
        val img8 = findViewById<ImageView>(R.id.imgALA18128)
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
        val nameProduct = "Filtro de Aire ALA - 17049"
        val type = "Filtro de Tipo Aire - HD"
        val descrip1 = "Dimensiones (MM): PESO: 0,785 / DIAMETRO: 95,5 / ALTURA: 176,7 / ROSCA: M20x1.5"
        val descrip2 = "Dimensiones (Pulgada): PESO: 0,03 / DIAMETRO: 3.76 / ALTURA: 6,96 / ROSCA: NaN"
        val descrip3 = "Dimensiones (Cm): PESO: 0,08 / DIAMETRO: 9.55 / ALTURA: 17.67 / ROSCA: NaN"
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
        val nameProduct = "Filtro de Aire ALA - 18139"
        val type = "Filtro de Tipo Aire - HD"
        val descrip1 = "Dimensiones (MM): PESO: 0,07 kilogramos / 0,16 libras/ DIAMETRO TOTAL: 73.0 / DIAMETRO INTERIOR: 40.2 /ALTURA: 97,8"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.00/ DIAMETRO TOTAL: 2,87 / DIAMETRO INTERIOR: 1,58 /ALTURA: 3.85"
        val descrip3 = "Dimensiones (Cm): PESO: 0.01/ DIAMETRO TOTAL: 7.30 / DIAMETRO INTERIOR: 4.02 /ALTURA: 9,78"
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
        val nameProduct = "Filtro de Aire ALA - 8928"
        val type = "Filtro de Tipo Aire - HD"
        val descrip1 = "Dimensiones (MM): PESO: 0,25 kilogramos / 0,55 libras/ DIAMETRO TOTAL: 95,4 / DIAMETRO INTERIOR: 23.0 /ALTURA: 127,5"
        val descrip2 = "Dimensiones (Pulgadas): PESO: 0.01/ DIAMETRO TOTAL: 3.76 / DIAMETRO INTERIOR: 0,91 /ALTURA: 5.02"
        val descrip3 = "Dimensiones (Cm): PESO: 0,02/ DIAMETRO TOTAL: 9.54 / DIAMETRO INTERIOR: 2.30 /ALTURA: 12.75"
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
        val nameProduct = "Filtro de Aire ALA - 18129"
        val type = "Filtro de Tipo Aire - HD"
        val descrip1 = "Dimensiones (MM): PESO: 0,62 Kilos / 1,36 Libras"
        val descrip2 = "Dimensiones (Pulgada): PESO: 0,02"
        val descrip3 = "Dimensiones (Cm): PESO: 0,06"
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
        val nameProduct = "Filtro de Aire ALA - 18126"
        val type = "Filtro de Tipo Aire - HD"
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
        val nameProduct = "Filtro de Aire ALA - 1807"
        val type = "Filtro de Tipo Aire - HD"
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
        val nameProduct = "Filtro de Aire ALA - 18120"
        val type = "Filtro de Tipo Aire - HD"
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
        val nameProduct = "Filtro de Aire ALA - 18128"
        val type = "Filtro de Tipo Aire - HD"
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
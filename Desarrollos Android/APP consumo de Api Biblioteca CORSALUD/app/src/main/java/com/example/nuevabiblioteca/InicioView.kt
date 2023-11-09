package com.example.nuevabiblioteca

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView


@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun InicioView(
    navController: NavController,
    viewModel: LibrosViewModel
) {
    val scope = rememberCoroutineScope()
    val libro by viewModel.libros.collectAsState()
    var searchTerm by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val busquedaCompleta = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(Constantes.COLOR_ROJO)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically) // Alinea el contenido verticalmente
                        .padding(6.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.library_icon),
                        contentDescription = "Alerta",
                        modifier = Modifier
                            .size(90.dp)
                            .padding(9.dp, 1.dp, 6.dp, 5.dp)
                    )
                }

                Text(
                    text = "Catálogo CORSALUD",
                    color = Color.White,
                    style = TextStyle(fontSize = 28.sp),
                    modifier = Modifier
                        .align(Alignment.CenterVertically) // Alinea el contenido verticalmente
                        .padding(2.dp, 16.dp, 34.dp, 0.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(Constantes.COLOR_BLANCO))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(Constantes.COLOR_AZUL_CIELO)) // Fondo azul detrás del buscador
                    .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            ) {
                BasicTextField(
                    value = searchTerm,
                    onValueChange = {
                        searchTerm = it
                        scope.launch {
                            viewModel.searchLibros(it)
                            // Actualiza busquedaCompleta cuando la búsqueda haya finalizado.
                            busquedaCompleta.value = true
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(16.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.White)
                        .padding(start = 8.dp)
                        .padding(top = 10.dp) // Ajusta el valor de top para centrar verticalmente
                )
            }

            if (searchTerm.isNotBlank()) {
                if (libro.isEmpty()) {
                    Text(
                        text = "Buscando resultados, sino hay coincidencias busque un término similar...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    // Agregar el GIF debajo del texto "Buscando resultados, sino hay coincidencias busque un término similar"
                    val context = LocalContext.current
                    val gifResource = R.drawable.brain // Reemplaza "brain" con el nombre de tu recurso de GIF en la carpeta "drawable"

                    AndroidView(
                        factory = { context ->
                            val imageView = ImageView(context)
                            imageView.setImageResource(gifResource)
                            val drawable = imageView.drawable
                            if (drawable is AnimatedImageDrawable) {
                                drawable.start()
                            }
                            imageView
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    placeable.placeRelative(0, -110) // Alinea en la parte superior y centro
                                }
                            }
                    )
                } else {
                    // Mostrar resultados cuando estén disponibles
                    LazyColumn {
                        items(libro) { libro ->
                            CardLibro(libro = libro) {
                                // Cuando se haga clic en el Card, se mostrará la descripción
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "              Ingrese un término de búsqueda",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.free), // Reemplaza "tu_imagen" con la referencia a tu imagen
                        contentDescription = "Imagen de búsqueda",
                        modifier = Modifier
                            .size(420.dp) // Ajusta el tamaño de la imagen según tus preferencias
                            .align(Alignment.TopCenter) // Centra la imagen en la parte superior
                    )
                }
            }
        }
    }
}
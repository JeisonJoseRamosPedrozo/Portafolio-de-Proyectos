package com.example.nuevabiblioteca

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    titulo: String
){
    TopAppBar(
        title = {
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(Constantes.COLOR_NEGRO_PERSONALIZADO)
        )
    )
}

@Composable
fun CardLibro(
    libro: LibroModel,
    onCardClick: () -> Unit
) {
    var showDescriptionDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .shadow(40.dp)
            .clickable {
                onCardClick()
                showDescriptionDialog = true
            }
    ) {
        Column(
            modifier = Modifier.background(Color(Constantes.COLOR_VERDE))
        ) {
            val imageUrl = "http://190.242.60.213:8383/OpacService/books/${libro.woResultadoOpacPK.ficha}.jpg"
            val painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    error(R.drawable.no_img) // Establece la imagen por defecto en caso de error
                }
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(650.dp)
            )

            Text(
                text = libro.titulo,
                fontWeight = FontWeight.ExtraBold,
                color = Color(Constantes.COLOR_ROJO_FUERTE),
                modifier = Modifier
                    .padding(12.dp)
            )
            Text(
                text = "Autor: ${libro.autor ?: "No disponible"}",
                color = Color.Black,
                modifier = Modifier.padding(12.dp)
            )

            Text(
                text = "ISBN: ${libro.isbn ?: "No disponible"}",
                color = Color.Black,
                modifier = Modifier.padding(12.dp)
            )
            Text(
                text = "Editorial: ${libro.editorial ?: "No disponible"}",
                color = Color.Black,
                modifier = Modifier.padding(12.dp)
            )
            Text(
                text = "Ficha: ${libro.woResultadoOpacPK.ficha ?: "No disponible"}",
                color = Color.Black,
                modifier = Modifier.padding(12.dp)
            )
        }
    }

    if (showDescriptionDialog) {
        AlertDialog(
            onDismissRequest = {
                showDescriptionDialog = false
            },
            title = {
                Text(
                    text = "Signatura Topográfica: " + (libro.signatura ?: "Descripción no disponible"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .background(Color(Constantes.COLOR_NARANJA))
                        .padding(16.dp)
                )
            },
            text = {
                Column {
                    Text(
                        text = "ISBN: " + (libro.isbn ?: "No disponible"),
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Autor: " + (libro.autor ?: "No disponible"),
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Título: " + (libro.titulo ?: "No disponible"),
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Edición: " + (libro.edicion ?: "No disponible"),
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Editorial: " + (libro.editorial ?: "No disponible"),
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "D.Física: " + (libro.dfisica ?: "No disponible"),
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },

            confirmButton = {
                Button(
                    onClick = {
                        showDescriptionDialog = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = "Aceptar",
                        color = Color.White
                    )
                }
            },
        )
    }
}
package com.example.apivideojuegos.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.apivideojuegos.modelos.VideoJuegosLista
import com.example.apivideojuegos.utilidades.Constantes

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
fun CardJuego(
    juego: VideoJuegosLista,
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
                showDescriptionDialog = true // Establece el estado como verdadero al hacer clic en el Card
            }
    ) {
        Column (
            modifier = Modifier.background(Color.Black) // Establece el fondo del texto como negro
        ) {
            InicioImagen(imagen = juego.imagen)
            Text(
                text = juego.name,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
    }

    if (showDescriptionDialog) {
        AlertDialog(
            onDismissRequest = {
                showDescriptionDialog = false // Cierra el cuadro de diálogo
            },
            title = {
                Text(
                    text = juego.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, // Tamaño de fuente
                    modifier = Modifier.padding(16.dp)
                )
            },
            text = {
                Text(
                    text = "Puntaje ofrecido por Metacritic:  " + (juego.descripcion ?: "Descripción no disponible"),
                    color = Color.Blue,
                    modifier = Modifier.padding(16.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDescriptionDialog = false // Cierra el cuadro de diálogo al hacer clic en el botón "Aceptar"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // Color de fondo del botón
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


@Composable
fun InicioImagen(imagen: String) {
    val imagenPainter = rememberImagePainter(data = imagen)

    Image(
        painter = imagenPainter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}


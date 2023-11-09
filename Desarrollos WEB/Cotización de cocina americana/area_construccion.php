<?php
    if (session_status() === PHP_SESSION_NONE) {
        session_start(); // Iniciar la sesión si aún no está activa
    }
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="area_construccion.css"> <!-- Enlaza el archivo CSS -->
    <link rel="icon" href="img/logo.png" type="image/png"> <!-- Establece el ícono de la página -->
    <script>
        // Script para aplicar la clase CSS a la imagen seleccionada
        window.addEventListener('DOMContentLoaded', () => {
            const images = document.querySelectorAll('.radio-options img'); // Obtiene todas las imágenes

            images.forEach(image => {
                image.addEventListener('click', () => {
                    // Elimina la clase 'selected-image' de todas las imágenes
                    images.forEach(img => img.classList.remove('selected-image'));
                    // Agrega la clase 'selected-image' a la imagen seleccionada
                    image.classList.add('selected-image');
                });
            });
        });
    </script>

    <title>Remodelando</title> <!-- Establece el título de la página -->
</head>


<body>
	<div class="btn-container">
		<div class="btn-wrapper">
			<a href="cerrar_sesion.php" class="btn">Cerrar sesión</a>
		</div>
	</div>

    <?php
    // Inicializar la variable $nextForm
$nextForm = 0;
$estufa = 30; // Valor de la estufa
$campana = 30; // Valor de la campana

if ($_SERVER['REQUEST_METHOD'] === 'POST') { // Verificar si se realizó una solicitud POST
    if ($_POST['enviar'] === 'Continuar al área de la pared B') {
        // Al hacer clic en el botón 'Continuar al área de la pared B'
        $_SESSION['nevera'] = intval($_POST['modelo']); // Guardar el valor seleccionado en la sesión
        $nextForm = 2; // Establecer el siguiente formulario a mostrar
    } elseif ($_POST['enviar'] === 'Calcular cotización') {
        // Al hacer clic en el botón 'Calcular cotización'
        $email = $_SESSION['correo']; // Obtener el correo electrónico de la sesión
        $nevera = $_SESSION['nevera']; // Obtener el valor de nevera de la sesión
        $lavavajillas = 24; // Valor del lavavajillas

        // Calcular las áreas de construcción
        $sumaObjetos = $nevera + $estufa + $campana;
        $areaDeConstruccionA = 200 - $sumaObjetos;
        $areaDeConstruccionB = 72 - $lavavajillas;
        $areDeConstruccionTotal = $areaDeConstruccionA + $areaDeConstruccionB;

        // Calcular el valor de la cotización
        $valorCotizacion = $areDeConstruccionTotal * 24;

        $conn = mysqli_connect("localhost", "root", "", "cotizacion"); // Conectar a la base de datos
        if ($conn) {
            // Insertar los datos en la tabla 'cotizar'
            $query = "INSERT INTO cotizar (email, nevera, estufa, campana, lavavajillas, cotizacion) VALUES ('$email',$nevera, $estufa, $campana, $lavavajillas, $valorCotizacion)";
            mysqli_query($conn, $query);
            mysqli_close($conn);
        } else {
            echo "Error en la conexión a la base de datos";
        }

        header('Location: cotizacion.php'); // Redirigir a la página de cotización
        exit;
    }
} else {
    $nextForm = 1; // Establecer el siguiente formulario a mostrar
}

// Mostrar el formulario correspondiente
if ($nextForm === 1) {
    // Mostrar el primer formulario para calcular el área de construcción de la pared A
    echo '<h1>Calcular área de construcción de la pared A</h1>
    <br><br>
    <form method="post">
        <label for="nevera">Nevera:</label>
        <br>
        <div class="radio-options">
            <label>
                <input type="radio" name="modelo" value="30" required>
                <br>
                <div class="image-wrapper">
                    <img src="img/neveraimg1.jpg">
                </div>
                <br>
                Modelo de 30 pulgadas -
            </label>
            <label>
                <input type="radio" name="modelo" value="32" required>
                <br>
                <div class="image-wrapper">
                    <img src="img/neveraimg2.jpg">
                </div>
                <br>
                Modelo de 32 pulgadas -
            </label>
            <label>
                <input type="radio" name="modelo" value="38" required>
                <br>
                <div class="image-wrapper">
                    <img src="img/neveraimg3.jpg">
                </div>
                <br>
                Modelo de 38 pulgadas
            </label>
        </div>
        <br>
        <br>
        <label for="estufa">Estufa:</label>
        <br>
        <div class="image-wrapper image-wrapper-estufa">
            <img src="img/estufaimg.jpg">
        </div>
        <br>
        <input type="text" name="estufa" value="Modelo de 30 pulgadas (Modelo único)" readonly>
        <br>
        <br>
        <label for="campana">Campana extractora:</label>
        <br>
        <div class="image-wrapper image-wrapper-campana">
            <img src="img/campanaimg.jpg">
        </div>
        <br>
        <input type="text" name="campana" value="Modelo de 30 pulgadas (Modelo único)" readonly>
        <br>
        <br>
        <div class="centered-button">
            <input type="submit" value="Continuar al área de la pared B" name="enviar">
        </div>
    </form>';

} elseif ($nextForm === 2) {
    // Mostrar el segundo formulario para calcular el área de construcción de la pared B
    echo '<h1>Calcular área de construcción de la pared B</h1>
    <br><br>
    <form method="post">
        <br>
        <label for="estufa">Lavavajillas:</label>
        <br>
        <div class="image-wrapper image-wrapper-estufa">
            <img src="img/lavavajillas.jpg">
        </div>
        <br>
        <input type="text" name="lavavajillas" value="Dishwasher de 24 pulgadas (Modelo único)" readonly>
        <br>
        <br>
        <div class="centered-button">
            <input type="submit" value="Calcular cotización" name="enviar">
        </div>
    </form>';
}

?>

</body>
</html>

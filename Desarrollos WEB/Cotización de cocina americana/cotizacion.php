<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start(); // Iniciar la sesión si aún no está activa
}

// Verifica si el botón "Realizar nueva simulación" ha sido presionado
if (isset($_POST['nueva_simulacion'])) {
    // Realiza la conexión a la base de datos
    $conn = mysqli_connect("localhost", "root", "", "cotizacion");
    if ($conn) {
        // Obtiene el correo electrónico del usuario de la sesión actual
        $email = $_SESSION['correo'];

        // Sanitiza el correo electrónico para evitar inyección SQL
        $email = mysqli_real_escape_string($conn, $email);

        // Realiza la consulta para obtener el email del usuario
        $query = "SELECT email FROM usuarios WHERE email = '$email'";
        $result = mysqli_query($conn, $query);

        if ($result && mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            $correo = $row['email'];

            // Elimina los datos de la tabla "cotizar" asociados al usuario
            $deleteQuery = "DELETE FROM cotizar WHERE email = '$correo'";
            mysqli_query($conn, $deleteQuery);

            // Verifica si se eliminaron filas de la tabla
            if (mysqli_affected_rows($conn) > 0) {
                echo "Los datos de cotización asociados al usuario han sido eliminados correctamente.";
            } else {
                echo "No se encontraron datos de cotización asociados al usuario.";
            }
        } else {
            echo "El usuario no fue encontrado en la tabla 'usuarios'.";
        }

        mysqli_close($conn);
        header('Location: area_construccion.php');
    }
}
?>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="img/logo.png" type="image/png">
    <style>
        /* Estilos generales del cuerpo del documento */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background: linear-gradient(rgb(188, 199, 224), rgb(182, 233, 218), rgb(174, 216, 221));
        }

        /* Estilos para el título principal */
        h1 {
            font-size: 48px;
            margin-bottom: 30px;
            margin-top: 9%;
            text-align: center;
            color: rgb(15, 3, 87);
            margin-top: 12%; /* Margen superior del título */
        }

        /* Estilos para los párrafos */
        p {
            font-size: 36px;
            margin-bottom: 50px;
            color: rgb(0, 0, 0);
            font-weight: bold;
            text-align: center;
        }

        /* Estilos para el contenedor de botones */
        .btn-container {
            position: fixed;
            top: 0;
            right: 0; /* Cambia la posición a la derecha */
            left: 0;
            height: 14%;
            background: linear-gradient(90deg, rgb(45, 207, 186) 5%, rgb(100, 214, 176) 35%, rgb(102, 226, 235) 60%);
            z-index: 999;
            display: flex;
            align-items: center;
            justify-content: flex-end; /* Cambia la alineación a la derecha */
            padding: 0 20px;
        }

        /* Estilos para los botones */
        .btn {
            background-color: #140350;
            color: #20d8e6;
            text-decoration: none;
            padding: 20px 50px;
            border-radius: 10px;
            font-size: 24px;
            transition: background-color 0.3s ease;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        /* Estilos al pasar el cursor por encima del botón */
        .btn:hover{
            background-color: #B2EBF2;
            color: black;
        }

        /* Estilos para el contenedor de botones */
        .btn-wrapper {
            display: flex;
            align-items: center;
        }

        /* Estilos para los botones dentro del contenedor */
        .btn-container .btn {
            margin-left: 10px;
        }

        /* Estilos para el primer botón dentro del contenedor */
        .btn-container .btn:first-child {
            margin-left: 0;
        }

        /* Estilos para el elemento de clase "pdf" */
        .pdf{
            margin-bottom: 10%;
            text-align: center;
        }

        /* Estilos para el contenedor interno de botones */
        .btn-container-inner {
            display: flex;
            align-items: center;
            justify-content: flex-end; /* Alinea los botones a la derecha */
            width: 100%;
        }

        /* Estilos para las imágenes */
        img{
            max-width: 650px; /* Establece el ancho máximo de la imagen */
            max-height: 650px; /* Establece el largo máximo de la imagen */
            width: auto; /* Permite que la imagen se redimensione proporcionalmente */
            height: auto; /* Permite que la imagen se redimensione proporcionalmente */
        }

        /* Estilos para el botón con borde */
        .btn-bordered {
            border: 2px solid #140350;
        }

        /* Estilos para el segundo botón */
        .btn2{
            background-color: #140350;
            color: #20d8e6;
            text-decoration: none;
            border-radius: 10px;
            padding: 20px 50px;
            font-size: 26px;
            transition: background-color 0.3s ease;
        }

        /* Estilos al pasar el cursor por encima del segundo botón */
        .btn2:hover{
            background-color:#B2EBF2;
            color:black;
        }

        @media (max-width: 768px) {
        /* Estilos para el título */
        h1 {
            margin-top: 29%; /* Ajusta el margen superior */
            font-size: 36px; /* Cambia el tamaño de fuente */
        }

        /* Estilos para el párrafo */
        p {
            font-size: 32px; /* Cambia el tamaño de fuente */
            margin-top: 40px; /* Ajusta el margen superior */
        }

        /* Estilos para el contenedor de botones */
        .btn-container {
            position: fixed;
            top: 0;
            right: 0; /* Cambia la posición a la derecha */
            left: 0;
            height: 20%; /* Cambia la altura */
            background: linear-gradient(90deg, rgb(45, 207, 186) 5%, rgb(100, 214, 176) 35%, rgb(102, 226, 235) 60%);
            z-index: 999;
            display: flex;
            align-items: center;
            justify-content: flex-end; /* Cambia la alineación a la derecha */
            padding: 0 20px;
        }

        /* Estilos para los botones */
        .btn {
            background-color: #140350;
            color: #20d8e6;
            text-decoration: none;
            border-radius: 10px;
            font-size: 0.77em; /* Cambia el tamaño de fuente */
            transition: background-color 0.3s ease;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        /* Estilos al pasar el cursor por encima de los botones */
        .btn:hover{
            background-color: #B2EBF2;
            color: black;
        }

        /* Estilos para el contenedor de botones con varios elementos */
        .btn-wrapper {
            display: flex;
            align-items: center;
        }

        /* Estilos para los botones dentro del contenedor */
        .btn-container .btn {
            margin-left: 10px;
        }

        /* Estilos para el primer botón dentro del contenedor */
        .btn-container .btn:first-child {
            margin-left: 0;
        }

        /* Estilos para el segundo botón */
        .btn2{
            background-color: #140350;
            color: #20d8e6;
            text-decoration: none;
            border-radius: 10px;
            font-size: 0.77em; /* Cambia el tamaño de fuente */
        }

        /* Estilos para el elemento con clase "pdf" */
        .pdf{
            margin-bottom: 10%;
        }

        /* Estilos para las imágenes */
        img{
            max-width: 300px; /* Establece el ancho máximo de la imagen */
            max-height: 300px; /* Establece la altura máxima de la imagen */
            width: auto; /* Permite que la imagen se redimensione proporcionalmente */
            height: auto; /* Permite que la imagen se redimensione proporcionalmente */
        }
    }

    </style>
    <title>Remodelando</title>
</head>

<body>
    <div class="btn-container">
        <div class="btn-wrapper">
            <div class="btn-container-inner"> <!-- Contenedor interno de botones -->
                <form action="" method="post">
                    <button type="submit" name="nueva_simulacion" class="btn">Realizar nueva simulación</button>
                </form>
            </div>
        </div>
        <a href="cerrar_sesion.php" class="btn btn-bordered">Cerrar sesión</a>
    </div>
    <br>

    <!-- Título -->
    <h1>Total de la cotización:</h1>

    <!-- Imagen -->
    <img src="img/imagencocina.jpg">

    <!-- Párrafo con datos de cotización -->
    <p><?php include('datoscotizacion.php');?></p>
    <br>

    <!-- Contenedor para los botones y enlace -->
    <div class="pdf">
        <!-- Enlace para solicitar una cita con un especialista -->
        <a href="cita.php" class="btn2 btn-bordered">Solicitar cita con un especialista</a>
        <br><br><br><br><br>
        <!-- Enlace para generar un PDF -->
        <a href="generarPDF.php" class="btn" target="_blank">Obtener PDF</a>
    </div>
    <br><br>

</body>
</html>


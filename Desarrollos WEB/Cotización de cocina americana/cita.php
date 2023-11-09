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
<html>
<head>
    <link rel="icon" href="img/logo.png" type="image/png">
    <title>Remodelando</title>
    <style>
        body {
            background-color: #f2f2f2;
            font-family: Arial, sans-serif;
            text-align: center;
        }
        
        .message {
            margin: 50px auto;
            padding: 20px;
            background-color: #cce6ff;
            color: #000066;
            font-size: 30px;
            max-width: 600px;
            width: 90%;
            border-radius: 5px;
            opacity: 0; /* Inicialmente, ocultar el cuadro de mensaje */
            transform: translateY(100px); /* Desplazar el cuadro hacia abajo */
            animation: slide-in 2.5s ease-out forwards; /* Animación de desplazamiento */
        }

        @keyframes slide-in {
            0% {
                opacity: 0;
                transform: translateY(100px);
            }
            100% {
                opacity: 1;
                transform: translateY(0);
            }
        }

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

        .btn:hover{
            background-color: #B2EBF2;
            color: black;
        }

        .btn-wrapper {
            display: flex;
            align-items: center;
        }

        .btn-container .btn {
            margin-left: 10px;
        }

        .btn-container .btn:first-child {
            margin-left: 0;
        }

        .btn-container-inner {
            display: flex;
            align-items: center;
            justify-content: flex-end; /* Alinea los botones a la derecha */
            width: 100%;
        }

        .btn-bordered {
            border: 2px solid #140350;
        }

        @media (max-width: 768px){
            .message {
                margin: 50px auto;
                padding: 20px;
                background-color: #cce6ff;
                color: #000066;
                font-size: 1.1em;
                max-width: 600px;
                width: 90%;
                border-radius: 5px;
            }

            .btn-container {
                position: fixed;
                top: 0;
                right: 0; /* Cambia la posición a la derecha */
                left: 0;
                height: 20%;
                background: linear-gradient(90deg, rgb(45, 207, 186) 5%, rgb(100, 214, 176) 35%, rgb(102, 226, 235) 60%);
                z-index: 999;
                display: flex;
                align-items: center;
                justify-content: flex-end; /* Cambia la alineación a la derecha */
                padding: 0 20px;
            }

            .btn {
                background-color: #140350;
                color: #20d8e6;
                text-decoration: none;
                padding: 20px 50px;
                border-radius: 10px;
                font-size: 19px;
                transition: background-color 0.3s ease;
                font-weight: bold;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .btn:hover{
                background-color: #B2EBF2;
                color: black;
            }

            .btn-wrapper {
                display: flex;
                align-items: center;
            }

            .btn-container .btn {
                margin-left: 10px;
            }

            .btn-container .btn:first-child {
                margin-left: 0;
            }

        }
    </style>
</head>
<body>
    <div class="btn-container">
        <div class="btn-wrapper">
            <div class="btn-container-inner"> <!-- Agrega la clase btn-container-inner -->
                <form action="" method="post">
                    <button type="submit" name="nueva_simulacion" class="btn">Realizar nueva simulación</button>
                </form>
            </div>
        </div>
        <a href="cerrar_sesion.php" class="btn btn-bordered">Cerrar sesión</a>
    </div>
    <br><br><br><br><br><br><br><br>

    <?php
    $conn = mysqli_connect("localhost", "root", "", "cotizacion");
    if ($conn) {
        $email = $_SESSION['correo'];

        // Verificar si ya existe un registro para el usuario
        $selectQuery1 = "SELECT fecha FROM cita WHERE email = '$email'";
        $selectQuery2 = "SELECT names FROM usuarios WHERE email = '$email'";
        $result1 = mysqli_query($conn, $selectQuery1);
        $result2 = mysqli_query($conn, $selectQuery2);

        if (($result1 && mysqli_num_rows($result1) > 0) && ($result2 && mysqli_num_rows($result2) > 0)){
            // Mostrar la fecha existente al usuario
            $row1 = mysqli_fetch_assoc($result1);
            $row2 = mysqli_fetch_assoc($result2);
            echo "<div class='message'>Muchas gracias por confiar en nuestra compañía estimado(a)
            " . $row2['names'] . ", el " . $row1['fecha'] . " recibirás una llamada de 
            uno de nuestros asesores especialistas en remodelamiento 
            de cocinas para resolver todas tus dudas, preguntas y posibles sugerencias. 
            En Remodelando esperamos por ti.</div>";
        } else {
            // Si no existe un registro, insertar la fecha
            $fecha = date('Y-m-d', strtotime('+3 days'));
            $query = "INSERT INTO cita (email, fecha) VALUES ('$email', '$fecha')";
            mysqli_query($conn, $query);

            // Mostrar la fecha al usuario
            echo "<div class='message'>Muchas gracias por confiar en nuestra compañía estimado(a)
            " . $row2['names'] . ", el " . $row1['fecha'] . " recibirás una llamada de 
            uno de nuestros asesores especialistas en remodelamiento 
            de cocinas para resolver todas tus dudas, preguntas y posibles sugerencias. 
            En Remodelando esperamos por ti.</div>";
        }

        // Eliminar las citas que tienen más de 3 días
        $deleteQuery = "DELETE FROM cita WHERE fecha < DATE_SUB(NOW(), INTERVAL 3 DAY)";
        mysqli_query($conn, $deleteQuery);

        mysqli_close($conn);
    } else {
        echo "<div class='message'>Error en la conexión a la base de datos</div>";
    }
?>


</body>
</html>

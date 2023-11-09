<?php
    if (session_status() === PHP_SESSION_NONE) {
        session_start(); // Iniciar la sesión si aún no está activa
    }

    $conn = mysqli_connect("localhost", "root", "", "cotizacion");

    if ($conn) {
        // Obtener el email del usuario actualmente logeado
        $email = $_SESSION['correo'];

        // Consulta SQL modificada con filtro por el usuario actual
        $query = "SELECT nevera, estufa, campana, lavavajillas, cotizacion FROM cotizar WHERE email = '$email'";
        $result = mysqli_query($conn, $query);

        if (mysqli_num_rows($result) > 0) {
            // Imprimir los valores de la consulta
            while ($row = mysqli_fetch_assoc($result)) {
                echo "Nevera: " . $row['nevera'] . " pulgadas<br><br>"; // Imprime el valor de la columna 'nevera'
                echo "Estufa: " . $row['estufa'] . " pulgadas<br><br>"; // Imprime el valor de la columna 'estufa'
                echo "Campana: " . $row['campana'] . " pulgadas<br><br>"; // Imprime el valor de la columna 'campana'
                echo "Lavavajillas: " . $row['lavavajillas'] . " pulgadas<br><br>"; // Imprime el valor de la columna 'lavavajillas'
                echo "Cotización: USD$" . $row['cotizacion'] . "<br>"; // Imprime el valor de la columna 'cotizacion'
            }
        } else {
            echo "No se encontraron resultados."; // Si no hay resultados, se imprime este mensaje
        }

        mysqli_close($conn); // Cierra la conexión a la base de datos
    } else {
        echo "Error en la conexión a la base de datos"; // Si no se pudo conectar a la base de datos, se imprime este mensaje
    }
?>

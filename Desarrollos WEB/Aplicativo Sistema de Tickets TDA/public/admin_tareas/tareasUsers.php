<?php
session_start();

// Conexión a la base de datos
$servername = "localhost";
$username = "root";
$password = "";
$database = "data_empresa";

$conn = new mysqli($servername, $username, $password, $database);
if ($conn->connect_error) {
  die("Error de conexión: " . $conn->connect_error);
}

// Obtener el identificador del usuario actualmente logueado
if (isset($_SESSION['identificacion'])) {
    $identificacion = $_SESSION['identificacion'];

    // Consulta SQL para obtener los datos del usuario actual
    $sql = "SELECT tarea, identificacion, fecha_inicio, hora_inicial, fecha_final, hora_final FROM tareas WHERE identificacion = '$identificacion'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        echo "
        <!doctype html>
        <html lang='en'>
          <head>
            <!-- Required meta tags -->
            <meta charset='utf-8'>
            <meta name='viewport' content='width=device-width, initial-scale=1'>

            <!-- Bootstrap CSS -->
            <link href='boostrap.css' rel='stylesheet'>
            <link rel='icon' href='/svg/logoTDA2.png' type='png'>
            <link rel='stylesheet' href='.datatable/datatable.css'>
            <link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css'>
            <link rel='stylesheet' href='stilos.css'>
            <link href='cdn_datatables.css' rel='stylesheet' type='text/css'>
            <title>Tareas asignadas</title>
          </head>
          <body>
            <table class='table'>
              <thead>
                <tr>
                  <th>Tarea</th>
                  <th>Fecha de inicio</th>
                  <th>Hora inicial</th>
                  <th>Fecha final</th>
                  <th>Hora final</th>
                </tr>
              </thead>
              <tbody>";
        // Mostrar los datos en la tabla
        while ($row = $result->fetch_assoc()) {
            echo "<tr>";
            echo "<td>" . $row["tarea"] . "</td>";
            echo "<td>" . $row["fecha_inicio"] . "</td>";
            echo "<td>" . $row["hora_inicial"] . "</td>";
            echo "<td>" . $row["fecha_final"] . "</td>";
            echo "<td>" . $row["hora_final"] . "</td>";
            echo "</tr>";
        }
        echo "</tbody>
            </table>
          </body>
        </html>";
    } else {
        echo "No se encontraron tareas asignadas.";
    }
} else {
    echo "El identificador de usuario no está definido en la sesión.";
}

$conn->close();
?>

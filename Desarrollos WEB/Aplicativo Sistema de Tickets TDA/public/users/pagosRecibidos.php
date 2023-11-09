<?php
// Establecer la conexión a la base de datos utilizando PDO
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "data_empresa";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo "Error de conexión: " . $e->getMessage();
}

// Obtener el número de identificación del usuario logeado desde la URL
if (isset($_GET['identificacion'])) {
    $identificacion = $_GET['identificacion'];

    // Realizar la consulta a la tabla de pagos utilizando el número de identificación
    $query = "SELECT monto FROM pagos WHERE identificacion = '$identificacion'";
    $registros = $conn->query($query);
    $datos = $registros->fetchAll(PDO::FETCH_ASSOC);

    // Generar el HTML de las filas de la tabla
    $html = "";
    foreach ($datos as $fila) {
        $monto = $fila['monto'];
        $monto_con_signo = "$" . $monto;
        $html .= "<tr><td style='width: 20%; background-color: #f5f5f5;'>$monto_con_signo</td></tr>";
    }
}
?>

<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="boostrap.css" rel="stylesheet">
    <link rel="icon" href="/svg/logoTDA2.png" type="png">
    <link rel="stylesheet" href=".datatable/datatable.css">
    <link rel="stylesheet" href="estilos.css">
    <link href="cdn_datatables.css" rel="stylesheet" type="text/css">
    <style>
        body {
            text-align: center;
            font-size: 28px;
            background-image: url('https://images.pexels.com/photos/906982/pexels-photo-906982.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');
            background-repeat: no-repeat;
            background-attachment: fixed;
            background-size: cover;
            font-family: 'figtree', sans-serif;
            width: 100%;
            height: 100%;
        }

        h1 {
            margin-top: 20px;
            margin-bottom: 30px;
            font-size: 44px;
        }

        table {
            margin: 0 auto;
            width: 80%;
            font-size: 30px;
        }

        th, td {
            padding: 10px;
        }

        th {
            background-color: green;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        input[type="file"] {
            margin-top: 10px;
        }

        button[type="submit"] {
            margin-top: 10px;
            padding: 8px 16px;
            font-size: 16px;
            background-color: #4caf50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button[type="submit"]:hover {
            background-color: #45a049;
        }

        p {
            margin-top: 20px;
        }
    </style>

    <title>Mis remuneraciones</title>
  </head>
  <body>
    <div class="container fondo">
        <br>
        <h1 class="text-center">Registro de remuneraciones</h1>
        <br><br>
        <div class="table-responsive">
            <table id="datos_usuario" class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Montos</th>
                    </tr>
                </thead>
                <tbody>
                    <?php echo $html; ?>
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- Bootstrap JavaScript -->
    <script src="jquery.min.js"></script>
    <script src="popper.min.js"></script>
    <script src="bootstrap.min.js"></script>

    <!-- DataTables JavaScript -->
    <script src=".datatable/datatable.js"></script>
    <script src="cdn_datatables.js"></script>
  </body>
</html>

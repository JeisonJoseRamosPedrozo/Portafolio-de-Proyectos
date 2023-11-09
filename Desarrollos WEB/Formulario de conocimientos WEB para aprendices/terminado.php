<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Evaluación Frontend</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            font-size: 16px;
            margin: 0;
            padding: 0;
            background: linear-gradient(rgb(137, 193, 195),rgb(69, 65, 148),rgb(68, 61, 124));
        }
        .container-fluid {
            max-width: 100%;
            margin-top: 5%;
        }
        .contenido {
            background: linear-gradient(rgb(137, 193, 195),rgb(69, 65, 148),rgb(68, 61, 124));
            border: 1px solid #ddd;
            padding: 20px;
            margin: 20px;
            border-radius: 5px;
            box-shadow: 0px 0px 10px #ddd;
        }
        .one{
            font-size: 52px;
            color: black;  
            font-weight: bold;
            margin: 0 0 20px;
            text-align: center;
            margin-bottom: 5%;
        }

        .two{
            font-size: 32px;
            background: linear-gradient(90deg, rgb(9, 64, 215) 5%, rgb(40, 13, 104) 35%, rgb(66, 0, 148) 60%);
            -webkit-background-clip: text;
            color: transparent;  
            font-weight: bold;
            margin: 0 0 20px;
            text-align: center;
        }

        .resultado {
            font-size: 24px;
            background: linear-gradient(to bottom, #55efc4, #00b894, #00cec9);
            border: 1px solid #ccc;
            padding: 20px;
            margin-bottom: 4%;
            margin-top: 5%;
            border-radius: 5px;
            text-align: center;
        }

        .cerrar-sesion {
            display: flex;
            justify-content: center;
        }

        .cerrar-sesion a {
            background: linear-gradient(135deg, #d1e3e3, #D5FFFF, #FFFFFF);
            text-decoration: none;
            background-color: #eee;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            transition: background-color 0.7s ease;
            color: black;
            font-weight: bold;
            font-size: 1.5em;
        }
        .cerrar-sesion a:hover {
            background: linear-gradient(45deg, #4a7515, #44f367, #6fd576, #0ec020);
        }
    </style>
</head>
<body class="container-fluid">
    <header>
        <h1 class="one">Resultados de la evaluación</h1>
    </header>
    <div class="contenido">
        <main>
            <h1 class="two">YA HAS REALIZADO LA EVALUACIÓN</h1>
            
            <?php
            // Iniciar sesión
            session_start();
        
            // Verificar si el usuario ha iniciado sesión
            if (!isset($_SESSION['id_usuario'])) {
                // Redirigir al usuario a la página de inicio de sesión si no ha iniciado sesión
                header('Location: iniciarsesion_antes.php');
                exit();
            }
        
            // Conectar a la base de datos
            $conn = new mysqli("localhost", "root", "", "registro");
        
            // Obtener el valor del campo "percentage" del usuario
            $id_usuario = $_SESSION['id_usuario'];
            $sql = "SELECT percentage FROM registro.result WHERE id='$id_usuario'";
            $resultado = $conn->query($sql);
            $usuario = $resultado->fetch_assoc();
            $percentage = $usuario['percentage'];
        
            // Cerrar la conexión a la base de datos
            $conn->close();
        
            // Mostrar el valor del campo "percentage"
            echo "<p class=\"resultado\">El resultado de tu evaluación fue de ".$percentage . "%, este resultado es irreversible.</p>";
        
            ?>
            <div class="cerrar-sesion">
                <a href="cerrarsesion.php">Cerrar sesión</a>
            </div>
        </main>
    </div

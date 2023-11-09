<style>
    .cerrar-sesion {
    bottom: 0;
    left: 0;
    width: 25%;
    background-color: blue;
    padding: 5px;
    text-align: center;
    box-sizing: border-box;
    border-top: 1px solid #ccc;
    display: flex;
    justify-content: center;
    margin-left: 37%;
    }

    .cerrar-sesion a {
    padding: 10px 20px;
    background: linear-gradient(90deg, rgb(38, 45, 40) 5%, rgb(32, 104, 4) 35%, rgb(5, 43, 13) 60%);
    color: #fff;
    text-decoration: none;
    border-radius: 5px;
    transition: background-color 0.3s ease;
    font-size: 1.6em;
    }

    .cerrar-sesion a:hover {
    background: linear-gradient(90deg, rgb(9, 64, 215) 5%, rgb(40, 13, 104) 35%, rgb(66, 0, 148) 60%);
    }

</style>


<!DOCTYPE html>
<html>
<head>
	<title>Evaluación Fronted</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="resultado.css">
</head>
<body>
	<header>
		<h1>Resultados de la evaluación</h1>
	</header>
	<main>
    <?php
    // En resultado-exito.php
    if (isset($_GET['porcentaje'])) {
        $porcentaje = $_GET['porcentaje'];
        if ($porcentaje >= 80) {
            echo "<div class='mensaje-exito'>¡Felicitaciones, has aprobado la evaluación con un porcentaje de " . $porcentaje . "%!</div>";
        } else {
            echo "<div class='mensaje-error'>Lo sentimos, has desaprobado la evaluación con un porcentaje de " . $porcentaje . "%</div>";
        }
    }
    ?>
    <div class="cerrar-sesion">
        <a href="cerrarsesion.php">Cerrar sesión</a>
    </div>
	</main>
</body>
</html>



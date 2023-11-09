
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
		<a href="cerrarsesion.php">Cerrar sesión</a>
	</header>
	<main>
	<?php
	// Establecer conexión a la base de datos
	$conexion = new mysqli('localhost', 'root', '', 'registro');

	// Verificar si hay errores de conexión
	if ($conexion->connect_error) {
		die('Error de conexión: ' . $conexion->connect_error);
	}

	// Obtener las variables de sesión creadas en "registrarse.php"
	session_start();
	$id = $_SESSION['id'];
	$name = $_SESSION['name'];

	$respuestas_correctas = array("c", "a", "b", "b", "a", "b", "a", "d", 
		"c", "a", "b", "c", "b", "c", "d", "a", "c", "d", "c", "c");

	$correctas = 0;

	for ($num_pregunta = 1; $num_pregunta <= 20; $num_pregunta++) {
		$respuesta = $_POST['p' . $num_pregunta];
		if ($respuesta == $respuestas_correctas[$num_pregunta-1]) {
			$correctas++;
		}
	}

	$porcentaje_correctas = $correctas / 20 * 100;

	// Insertar el resultado en la tabla "result"
	$insertar_resultado = "INSERT INTO result (id, name, percentage) VALUES ('$id', '$name', '$porcentaje_correctas')";
	if ($conexion->query($insertar_resultado) === TRUE) {
		// Redirigir al usuario a la página de éxito utilizando Post/Redirect/Get
		header('Location: resultado-exito.php?porcentaje=' . $porcentaje_correctas);
		exit();
	} else {
		// Redirigir al usuario a la página de error utilizando Post/Redirect/Get
		header('Location: resultado-error.php?error=' . urlencode($conexion->error));
		exit();
	}

	?>

	</main>
</body>
</html>

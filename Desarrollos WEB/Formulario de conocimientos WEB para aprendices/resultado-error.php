
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
    // En resultado-error.php
    if (isset($_GET['error'])) {
        $error = $_GET['error'];
        echo "<div class='mensaje-error'>Ha ocurrido un error al registrar el resultado de la evaluación: " . htmlspecialchars($error) . "</div>";
    }
    ?>
	</main>
</body>
</html>
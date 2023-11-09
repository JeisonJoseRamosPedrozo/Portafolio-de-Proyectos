<style>
    .error-message {
    background-color: #f8d7da;
    color: #721c24;
    padding: 10px;
    border: 1px solid #f5c6cb;
    border-radius: 5px;
    text-align: center;
    font-size: 2em;
}
</style>

<?php

session_start(); // Iniciar la sesión

// Conectar a la base de datos
$conn = new mysqli("localhost", "root", "", "cotizacion");

if ($conn->connect_error) {
    die("Error al conectarse a la base de datos: " . $conn->connect_error);
}

if (isset($_POST['enviar'])) {
    $email = $_POST['correo']; // Obtener el valor del correo electrónico del formulario
    $_SESSION['correo'] = $email;

    // Realizar una búsqueda en la base de datos
    $sql = "SELECT * FROM cotizacion.usuarios WHERE email='$email'";
    $resultado = $conn->query($sql);

    // Verificar si se encontró un usuario con las mismas credenciales
    if ($resultado->num_rows > 0) {
        // Guardar información del usuario en variables de sesión
        $usuario = $resultado->fetch_assoc();
        // Iniciar sesión
        session_start();
        header('Location: cotizacion.php');
        exit(); // Se recomienda agregar exit() después de redireccionar con header()
    } else {
        // Mostrar mensaje de error y requerir registro
        echo '<div class="error-message">El correo electrónico no existe.</div>';
        // ... código para redirigir al usuario a la página de registro, por ejemplo
    }
}

// Cerrar la conexión a la base de datos
$conn->close();
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="inicio_registro.css">
    <link rel="icon" href="img/logo.png" type="image/png">
    <title>Remodelando</title>
</head>
<body>
    <div class="container">
    <h1>Iniciar Sesión</h1>
    
    <form method="post">
    <!-- Formulario de inicio de sesión -->
    <label for="correo">Correo Electrónico:</label>
    <input type="email" name="correo" required> <!-- Campo para ingresar el correo electrónico -->
    <div style="text-align: center; margin-top: 20px;">
    <input type="submit" value="Confirmar" name="enviar"> <!-- Botón de enviar el formulario -->
    </div>

    <br>
    <div class="registrar">
        <a href="registro.php" class="btn">Aún no me he registrado, quiero registrarme ahora</a> <!-- Enlace para dirigir al usuario a la página de registro -->
    </div>
    </form>

    </div>
</body>
</html>

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

// Conectar a la base de datos
$conn = new mysqli("localhost", "root", "", "registro");


if(isset($_POST['enviar'])){
    $email= $_POST['correo'];
    $code_register= $_POST['codigo'];

    // Verificar conexión
    if ($conn->connect_error) {
        die("Error al conectarse a la base de datos: " . $conn->connect_error);
    }

    // Realizar una búsqueda en la base de datos
    $sql = "SELECT * FROM registro.register WHERE email='$email' AND code_register='$code_register'";
    $resultado = $conn->query($sql);

    // Verificar si se encontró un usuario con las mismas credenciales
    if ($resultado->num_rows > 0) {
        // Permitir que el usuario inicie sesión
        header('Location: evaluacion.php');
    } else {
        // Mostrar mensaje de error y requerir registro
        echo '<div class="error-message">correo electrónico o código de registro incorrecto.</div>'; 
        // ... código para redirigir al usuario a la página de registro, por ejemplo
    }

    // Cerrar la conexión a la base de datos
    $conn->close();

}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="forms.css">
    <title>Evaluación Frontend</title>
</head>
<body>
    <div class="container">
    <h1>Iniciar Sesión</h1>
    
    <form method="post">
    <label for="correo">Correo Electrónico:</label>
    <input type="email" name="correo" required>
    <label for="codigo">Tu código de registro:</label>
    <input type="text" name="codigo" required>
    <div style="text-align: center; margin-top: 20px;">
    <input type="submit" value="Confirmar" name="enviar">
    </div>

    <br>
    </form>

    </div>
</body>
</html>



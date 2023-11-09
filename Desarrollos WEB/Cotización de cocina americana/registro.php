<style>
    .error-message {
    background-color: #f8d7da;
    color: #721c24;
    padding: 3px;
    border: 1px solid #f5c6cb;
    border-radius: 5px;
    text-align: center;
    font-size: 2em;
}
</style>

<?php
    session_start(); // Iniciar la sesión

    $server= "localhost";
    $user= "root";
    $password= "";
    $dbname= "cotizacion";

    $conn = mysqli_connect($server, $user, $password, $dbname);

    if(isset($_POST['enviar'])){ // Verificar si se ha enviado el formulario
        $names= $_POST['nombre']; // Obtener el valor del campo "nombre" del formulario
        $email= $_POST['correo']; // Obtener el valor del campo "correo" del formulario
        $numberphone= $_POST['cel']; // Obtener el valor del campo "cel" del formulario
        $city= $_POST['ciudad']; // Obtener el valor del campo "ciudad" del formulario
        $departament= $_POST['departamento']; // Obtener el valor del campo "departamento" del formulario
        $direccion= $_POST['direccion']; // Obtener el valor del campo "direccion" del formulario

        $_SESSION['correo'] = $email; // Almacenar el valor del correo electrónico en una variable de sesión global

        // Construir la consulta SQL para insertar los datos del usuario en la tabla "usuarios" de la base de datos "cotizacion"
        $insertardatos_usuarios = "INSERT INTO usuarios (names, email, numberphone, city, departament, direccion) VALUES('$names','$email','$numberphone','$city','$departament','$direccion')";

        // Ejecutar la consulta SQL
        $ejecutar_usuarios = mysqli_query($conn, $insertardatos_usuarios);

        if (!$ejecutar_usuarios) { // Verificar si la ejecución de la consulta falla
            die('Error al insertar datos en la base de datos: ' . mysqli_error($conn)); // Mostrar mensaje de error
        }

        // Redirigir al usuario a otra página
        header('Location: area_construccion.php');
        exit; // Detener la ejecución del código después de la redirección
    }
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
    <br><br><br><br><br><br>
    <div class="container">
    <h1>REGISTRARME</h1>
    
    <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" name="registro">
    <!-- Formulario de registro -->
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" required> <!-- Campo para ingresar el nombre -->
    <label for="correo">Correo Electrónico:</label>
    <input type="email" name="correo" required> <!-- Campo para ingresar el correo electrónico -->
    <label for="cel">Número de teléfono:</label>
    <input type="number" name="cel" required> <!-- Campo para ingresar el número de teléfono -->
    <label for="ciudad">Ciudad:</label>
    <input type="text" name="ciudad" required> <!-- Campo para ingresar la ciudad -->
    <label for="departamento">Departamento:</label>
    <input type="text" name="departamento" required> <!-- Campo para ingresar el departamento -->
    <label for="direccion">Dirección:</label>
    <input type="text" name="direccion" required> <!-- Campo para ingresar la dirección -->
    <div style="text-align: center; margin-top: 20px;">
    <input type="submit" value="Confirmar" name="enviar"> <!-- Botón de enviar el formulario -->
    </div>

    <br>
    <div class="registrar">
        <a href="iniciosesion.php" class="btn">Ya me registré antes, quiero iniciar sesión ahora</a> <!-- Enlace para iniciar sesión -->
    </div>
    </form>

    </div>
    <br><br><br><br><br><br>
</body>
</html>





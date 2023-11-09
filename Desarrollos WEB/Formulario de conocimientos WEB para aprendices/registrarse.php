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

    $server= "localhost";
    $user= "root";
    $password= "";
    $dbname= "registro";

    $conn = mysqli_connect($server, $user, $password, $dbname);

    if(isset($_POST['enviar'])){
        $id= $_POST['id'];
        $name= $_POST['nombre(s)'];
        $lastnames= $_POST['apellidos'];
        $birthdate= $_POST['fechaDeNacimiento'];
        $age= $_POST['edad'];
        $email= $_POST['correo'];
        $code_register= $_POST['codigo'];
        
        if (strlen($id) > 10 || strlen($id) < 7) {
            echo '<div class="error-message">Tu número de identificación debe ser entre 7 y 10 dígitos, por lo tanto no fue posible realizar el registro.</div>';
        } elseif (strlen($age) > 2){
            echo '<div class="error-message">Tu edad parece ser incorrecta, corrige tu registro.</div>';
        }elseif (strlen($age) < 1){
            echo '<div class="error-message">No te puedes registrar sin colocar la edad, corrige tu registro.</div>'; 
        }else{
            // Guardar las variables en la variable superglobal $_SESSION
            $_SESSION['id'] = $id;
            $_SESSION['name'] = $name;

            $insertardatos_register = "INSERT INTO register (id, names, last_names, birthdate, age, email, code_register) VALUES('$id','$name','$lastnames','$birthdate','$age','$email','$code_register')";

            $ejecutar_register = mysqli_query($conn, $insertardatos_register);

            if (!$ejecutar_register) {
                die('Error al insertar datos en la base de datos: ' . mysqli_error($conn));
            }

            header('Location: iniciarsesion_despues.php');
            exit;
        }
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
    <br><br><br><br><br><br><br><br>
    <div class="container">
    <h1>REGISTRARME</h1>

    <!-- El formulario tiene un método de envío post, lo que significa que los datos 
    se enviarán al servidor en segundo plano y no se mostrarán en la URL. 
    El atributo action especifica la página a la que se enviará el formulario 
    una vez que se envíe. -->
    
    <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" name="registro">

    <!-- Los siguientes fragmentos de código son etiquetas label que muestran un mensaje 
    para el usuario y un campo de entrada donde el usuario puede ingresar sus datos
    de identificación. El atributo name se utilizará para identificar el campo de entrada 
    en el servidor.
     -->
    <label for="id">Número de identificación:</label>
    <input type="number" name="id" required>
    <label for="nombres(s)">Nombre(s):</label>
    <input type="text" name="nombre(s)" required>
    <label for="apellidos">Apellidos:</label>
    <input type="text" name="apellidos" required>
    <label for="fechaDeNacimiento">Fecha de Nacimiento:</label>

    <!-- La etiqueta onchange se utiliza para llamar a la función actualizarEdad() cada vez que 
    se cambia la fecha de nacimiento. -->

    <input type="date" id="fechaDeNacimiento" name="fechaDeNacimiento" required onchange="actualizarEdad()">

    <script>
    function actualizarEdad() {
    var fechaNacimiento = new Date(document.getElementById("fechaDeNacimiento").value);
    var fechaActual = new Date();
    var edad = Math.floor((fechaActual - fechaNacimiento) / (1000 * 60 * 60 * 24 * 365.25));
    document.getElementById("edad").value = edad;
    }
    </script>

    <!-- En el campo de edad no se introduce nada y se utiliza el atributo readonly para
    que el usuario no pueda editar o modificar su edad, ya que esta se calcula
    automáticamente en el código javascript de arriba, y cuando se llama al onchange
    de la fecha de nacimiento el campo de la edad se modifica de acuerdo al cálculo. -->

    <label for="edad">Edad:</label>
    <input type="number" name="edad" id="edad" max="120" min="5" readonly>

    <label for="correo">Correo Electrónico:</label>
    <input type="email" name="correo" required>

    <!-- Se genera un código de registro aleatorio. Se crea un arreglo de letras del alfabeto 
    y se selecciona aleatoriamente una letra de este arreglo para las primeras tres 
    posiciones del código. También se generan tres números aleatorios para las últimas 
    tres posiciones del código. Luego, se concatenan estos caracteres para formar el código 
    completo. -->

    <?php
    $letras = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 
    'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
    $num1 = rand(0, 9);
    $num2 = rand(0, 9);
    $num3 = rand(0, 9);
    $letra1 = $letras[array_rand($letras)];
    $letra2 = $letras[array_rand($letras)];
    $letra3 = $letras[array_rand($letras)];
    $codigoRegistro = $letra1 . $letra2 . $letra3 . $num1 . $num2 . $num3;
    ?>

    <!-- Se crea un elemento de entrada de texto HTML <input> con el atributo type 
    establecido en "text", el atributo name establecido en "codigo", y el atributo 
    value establecido en el código de registro generado en el script de php de arriba. 
    Además, se establece el atributo readonly para que el usuario no pueda editar 
    el código de registro. -->

    <label for="codigo">Código de registro:</label>
    <input type="text" name="codigo" value="<?php echo $codigoRegistro; ?>"readonly>
    <p>Guarda el código de registro generado para ti, este (<?php echo $codigoRegistro; ?>) será tu código de usuario por siempre</p>
    <div style="text-align: center; margin-top: 20px;">

    <!-- Se crea un elemento de entrada de botón HTML <input> con el atributo type 
    establecido en "submit", el atributo name establecido en "enviar", y el atributo 
    value establecido en "Registrar". Cuando se hace clic en este botón, se envía el 
    formulario al archivo PHP card.php para su procesamiento. -->

    <input type="submit" value="Registrar" name="enviar">
    </div>

    <br>
    <div class="registrar">
        <a href="iniciarsesion_antes.php" class="btn">Ya me registré antes, quiero iniciar sesión ahora</a>
    </div>
    </form>

    </div>
    <br><br><br><br><br><br><br><br>
</body>
</html>




<?php
session_start(); // Inicia la sesión
session_unset(); // Elimina todas las variables de sesión
session_destroy(); // Destruye la sesión
header("Location: index.php"); // Redirecciona al archivo index.php
exit(); // Termina la ejecución del script actual
?>

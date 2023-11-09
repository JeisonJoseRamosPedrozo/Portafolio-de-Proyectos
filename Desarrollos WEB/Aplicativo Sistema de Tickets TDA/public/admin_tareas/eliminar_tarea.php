<?php
// Archivo eliminar_tarea.php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Obtener el identificador de la tarea enviado desde el cliente
    $idTarea = $_POST["idTarea"];

    // Realizar las operaciones necesarias para eliminar la tarea del archivo
    // Puedes utilizar una lógica que marque la tarea como eliminada en el archivo

    // Ejemplo:
    $rutaArchivo = "ruta/del/archivo/tareas.txt";
    $tareasEliminadas = array();

    // Leer las tareas eliminadas previamente del archivo, si existen
    if (file_exists($rutaArchivo)) {
        $tareasEliminadas = file($rutaArchivo, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
    }

    // Agregar la tarea actual a la lista de tareas eliminadas
    if (!in_array($idTarea, $tareasEliminadas)) {
        $tareasEliminadas[] = $idTarea;
        file_put_contents($rutaArchivo, implode(PHP_EOL, $tareasEliminadas));
        echo "La tarea se eliminó del archivo correctamente.";
    } else {
        echo "La tarea ya estaba eliminada en el archivo.";
    }
}
?>

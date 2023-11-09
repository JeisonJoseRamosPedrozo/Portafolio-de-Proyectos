<!doctype html>
<html lang="en">
<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Bootstrap CSS -->
  <link href="boostrap.css" rel="stylesheet">
  <link rel="icon" href="/svg/logoTDA2.png" type="png">
  <link rel="stylesheet" href=".datatable/datatable.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
  <link rel="stylesheet" href="stilos.css">
  <link href="cdn_datatables.css" rel="stylesheet" type="text/css">
  <title>Administrador de Tareas</title>
</head>
<body>
<div class="container fondo">
  <h1 class="text-center">Administrador de tareas</h1>
  <div class="row">
    <div class="col-2 offset-10">
      <div class="text-center">
        <button type="button" class="btn btn-primary w-100" data-bs-toggle="modal" data-bs-target="#modalUsuario"
                id="botonCrear" name="botonCrear"> <i class="bi bi-plus-circle-fill">Crear</i></button>
      </div>
    </div>
  </div><br><br>
  <div class="table-responsive">
    <table id="datos_usuario" class="table table-bordered table-striped">
      <thead>
      <tr>
        <th>Tareas</th>
        <th>Id empleado</th>
        <th>Suprimir de esta tabla</th>
        <th>Estado</th>
      </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>

<!-- Modal -->
<div class="modal fade" id="modalUsuario" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Registro de tareas</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
        </button>
      </div>
      <div class="modal-body">
        <form method="POST" id="formulario" enctype="multipart/form-data">
          <label for="tarea">Ingresa la tarea</label>
          <textarea name="tarea" rows="10" cols="40" class="form-control" require></textarea>
          <label for="tarea">Id empleado</label>
          <input type="text" id="empleado" name="empleado" class="form-control" require>
        </form>
      </div>
      <div class="modal-footer">
        <input type="hidden" name="id_usuario" id="id_usuario">
        <input type="hidden" name="operacion" id="id_usuario">
        <input type="submit" name="action" id="action" class="btn btn-success" value="Crear">
      </div>
    </div>
  </div>
</div>


<!-- Optional JavaScript; choose one of the two! -->

<!-- Option 1: Bootstrap Bundle with Popper -->
<script src="datatable/jquery.dataTables.min.js"></script>
<script src="boostrap.js"></script>
<script src="query.js"></script>
<script src="cdn_datatables.js" type="text/javascript"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $("#action").on('click', function () {
      $("#formulario").submit();
    });

    // Obtener los datos de la tabla al cargar la página
    $.ajax({
      url: "obtenerRegistros.php",
      method: "GET",
      dataType: "json",
      success: function (data) {
        var tabla = $("#datos_usuario tbody");
        $.each(data, function (index, element) {
          tabla.append("<tr class='fila-tarea'><td> " + element.tarea + "</td><td>" + element.identificacion + "</td><td> <button type='button' name='eliminacion' class='btn btn-danger btn-eliminar'><i class='bi bi-trash-fill'></i> Eliminar</button></td><td> " + element.conclusion + "</td></tr>");
        });

        // Comprobar el estado de eliminación al cargar la página
        $(".fila-tarea").each(function () {
          var tareaId = $(this).find("td:first").text();
          if (localStorage.getItem("tareaEliminada-" + tareaId) === "true") {
            $(this).hide();
          }
        });
      },
      error: function (jqXHR, textStatus, errorThrown) {
        console.log("Error al obtener los datos: " + textStatus);
      }
    });

    // Escuchar el evento de clic en el botón de eliminar
    $("#datos_usuario").on("click", ".btn-eliminar", function () {
      var fila = $(this).closest(".fila-tarea");
      fila.hide(); // Ocultar la fila

      // Almacenar el estado de eliminación en el almacenamiento local
      var tareaId = fila.find("td:first").text(); // Obtener el identificador único de la tarea (puedes ajustar esto según tu estructura de datos)
      localStorage.setItem("tareaEliminada-" + tareaId, "true");
    });
  });
</script>


<?php
include("conexionSql.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $tarea = $_POST["tarea"];
  $empleado = $_POST["empleado"];

  // Consulta a la base de datos
  $consulta1 = $conn->query("INSERT INTO tareas(tarea, identificacion, estado, terminado, conclusion) VALUES ('" . $tarea . "', '" . $empleado . "', 'Pendiente', 'En espera...', 'En curso...');");
  $consulta1 = $conn->query("INSERT INTO historial(tarea, identificacion, estado, terminado, conclusion) VALUES ('" . $tarea . "', '" . $empleado . "', 'Pendiente', 'En espera...', 'En curso...');");
  $consulta2 = $conn->query("INSERT INTO papelera_tareas(tarea, identificacion, estado) VALUES ('" . $tarea . "', '" . $empleado . "', 'Pendiente');");
  $consulta3 = $conn->query("INSERT INTO pendientes(tarea, estado) VALUES ('" . $tarea . "', 'Pendiente');");
}
?>

<!-- Option 2: Separate Popper and Bootstrap JS -->
<!--
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
        integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
        integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
        crossorigin="anonymous"></script>
-->
</body>
</html>

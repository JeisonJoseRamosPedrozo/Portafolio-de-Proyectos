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
    <title>Historial de Actividades</title>
  </head>
  <body>
    <div class="container fondo">
        <h1 class="text-center">Historial de Actividades</h1>
        <br><br>
        <div class="table-responsive">
            <table id="datos_usuario" class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Tareas</th>
                        <th>Identificación</th>
                        <th>Estado</th>
                        <th>Fecha Inicial</th>
                        <th>Hora Inicial</th>
                        <th>Fecha Final</th>
                        <th>Hora Final</th>
                        <th>Resultado final</th>
                    </tr>
                    <tbody>
                    </tbody>
                </thead>
            </table>
        </div>
    </div>


<!-- Optional JavaScript; choose one of the two! -->

<!-- Option 1: Bootstrap Bundle with Popper -->
<script src="datatable/jquery.dataTables.min.js"></script>
<script src="boostrap.js"></script>
<script src="query.js"></script>
<script src="cdn_datatables.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
  $("#action").on('click', function() {
    $("#formulario").submit();
  });

  // Obtener los datos de la tabla al cargar la página
  $.ajax({
    url: "obtenerRegistros.php",
    method: "GET",
    dataType: "json",
    success: function(data) {
      var tabla = $("#datos_usuario tbody");
      $.each(data, function(index, element) {
        tabla.append("<tr><td> " + element.tarea + "</td><td>" + element.identificacion + "</td><td>" + element.estado + "</td><td>" + element.fecha_inicio+ "</td><td>" + element.hora_inicial + "</td><td>" + element.fecha_final + "</td><td>" + element.hora_final + "</td><td>" + element.conclusion + "</td></tr>");
      });
    },
    error: function(jqXHR, textStatus, errorThrown) {
      console.log("Error al obtener los datos: " + textStatus);
    }
  });
});
</script>
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
    <title>Supervisor de tareas</title>
</head>
<body>
<div class="container fondo">
    <h1 class="text-center">Supervisor de tareas</h1>
    <br><br>
    <div class="table-responsive">
        <table id="datos_usuario" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>Tareas</th>
                <th>Id empleado</th>
                <th>Estado</th>
                <th>Evidencias</th>
                <th>Terminado</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
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
    $.noConflict(); // Evitar conflictos entre jQuery y DataTables

    jQuery(document).ready(function($) {
        // ...

        // Obtener los datos de la tabla al cargar la página
        $.ajax({
            url: "obtenerRegistros.php",
            method: "GET",
            dataType: "json",
            success: function(data) {
                var tabla = $("#datos_usuario tbody");
                $.each(data, function(index, element) {
                    var downloadLink = '';
                    if (element.evidencia) {
                        // Generar el enlace de descarga del PDF
                        downloadLink = "<a href='/storage/" + element.evidencia + "' download>Descargar ticket terminado</a>";
                    } else {
                        // Mostrar un mensaje alternativo si no hay evidencia disponible
                        downloadLink = "No disponible";
                    }

                    tabla.append("<tr><td>" + element.tarea + "</td><td>" + element.identificacion + "</td><td>" + element.estado + "</td><td>" + downloadLink + "</td><td>" + element.terminado + "</td></tr>");
                });

                // Inicializar la tabla DataTables
                $("#datos_usuario").DataTable({
                    "destroy": true, // Destruir la instancia existente antes de crear una nueva
                    "retrieve": true, // Recuperar la instancia existente si ya ha sido inicializada
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error al obtener los datos: " + textStatus);
            }
        });
    });
</script>

</body>
</html>

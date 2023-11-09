<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="bootstrap.css" rel="stylesheet">
    <link rel="icon" href="/svg/logoTDA2.png" type="png">
    <link rel="stylesheet" href=".datatable/datatable.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link rel="stylesheet" href="estilos.css">
    <link href="cdn_datatables.css" rel="stylesheet" type="text/css">

    <style>
        body {
            text-align: center;
            font-size: 28px;
            background-image: url('https://images.pexels.com/photos/6199455/pexels-photo-6199455.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');
            background-repeat: no-repeat;
            background-attachment: fixed;
            background-size: cover;
            font-family: 'figtree', sans-serif;
            width: 100%;
            height: 100%;
        }

        h1 {
            margin-top: 20px;
            margin-bottom: 30px;
            font-size: 34px;
        }

        table {
            margin: 0 auto;
            width: 80%;
            font-size: 16px;
        }

        th, td {
            padding: 10px;
        }

        th {
            background-color: #f2f2f2;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        input[type="file"] {
            margin-top: 10px;
        }

        button[type="submit"] {
            margin-top: 10px;
            padding: 8px 16px;
            font-size: 16px;
            background-color: #4caf50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button[type="submit"]:hover {
            background-color: #45a049;
        }

        p {
            margin-top: 20px;
        }
    </style>

    <title>Tareas pendientes</title>
</head>
<body>
<div class="container">
    <br><br><br><br>
    <h1>Tareas pendientes</h1>

    @if(count($tareas) > 0)
        <table class="table">
            <thead>
            <tr>
                <th>Tarea</th>
                <th>Fecha de inicio</th>
                <th>Hora de inicio</th>
                <th>Fecha de finalización</th>
                <th>Hora de finalización</th>
            </tr>
            </thead>
            <tbody>
            @foreach($tareas as $tarea)
                @if($tarea->estado === 'Aceptado')
                    <tr>
                        <td>{{ $tarea->tarea }}</td>
                        <td>{{ $tarea->fecha_inicio }}</td>
                        <td>{{ $tarea->hora_inicial }}</td>
                        <td>{{ $tarea->fecha_final }}</td>
                        <td>{{ $tarea->hora_final }}</td>
                    </tr>
                    <tr>
                        <td colspan="5">
                            <form action="{{ route('tareas.evidencias') }}" method="POST" id="formulario{{ $tarea->id_tarea }}" enctype="multipart/form-data">
                                @csrf
                                <!-- Aquí puedes agregar los campos adicionales que necesites para el formulario -->
                                <input type="hidden" name="tareaId" value="{{ $tarea->id_tarea }}">
                                <input type="file" name="evidencia" accept="application/pdf" required>
                                <button type="submit">Entregar recibo de evidencia</button>
                            </form>
                        </td>
                    </tr>
                @endif
            @endforeach
            </tbody>
        </table>
    @else
        <p>No tienes tareas pendientes.</p>
    @endif
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $('button[type="submit"]').click(function(e) {
            e.preventDefault(); // Evitar el envío del formulario

            var formId = $(this).closest('form').attr('id'); // Obtener el ID del formulario

            // Generar la URL de la ruta fuera de la cadena de texto
            var eliminarPendienteUrl = '{{ route("tareas.eliminarPendiente") }}';

            // Concatenar la URL generada dentro de la llamada AJAX
            $.ajax({
                url: eliminarPendienteUrl,
                type: 'POST',
                data: $('#' + formId).serialize(),
                success: function(response) {
                    $('#' + formId).submit(); // Enviar el formulario después de eliminar la tarea
                },
                error: function(xhr, status, error) {
                    console.log(error);
                }
            });
        });
    });
</script>

</body>
</html>

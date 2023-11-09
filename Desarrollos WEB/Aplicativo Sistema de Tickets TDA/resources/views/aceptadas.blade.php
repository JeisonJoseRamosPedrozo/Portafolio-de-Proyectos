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
    <title>Tareas asignadas</title>
    <style>
      body {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        font-size: 30px;
        background-image: url('https://images.pexels.com/photos/14734003/pexels-photo-14734003.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');
        background-repeat: no-repeat;
        background-attachment: fixed;
        background-size: cover;
        width: 100%;
        height: 100%;
      }

      .container {
        margin-bottom: 20px;
        margin-right: 5%;
      }

      table {
        background-color: white;
        border-radius: 8px;
        padding: 20px;
        font-family: Arial, sans-serif;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      }

      th {
        font-weight: bold;
      }

      td {
        padding: 10px;
      }

      form {
        text-align: center;
        margin-top: 20px;
        color: white;
      }

      p {
        font-weight: bold;
        color: white;
      }

      button {
        padding: 10px 20px;
        background-color: blue;
        color: white;
        border: none;
        border-radius: 4px;
        font-size: 26px;
        cursor: pointer;
      }

      button:hover {
        background-color: #5b9ea8;
      }
    </style>
  </head>
  <body>
    <form action="{{ route('tareas.actualizar') }}" method="POST" id="formulario">
        @csrf
        <input type="hidden" name="tareaId" value="{{ session('ultima_tarea_id') }}">
        <br><br><br>
        <p>Seleccionar el tiempo de trabajo:</p>
        <label for="fecha_inicio">Ingresa la fecha de inicio</label>
        <input type="date" id="inicio" name="inicio" class="form-control" required>
        <br>
        <label for="tiempoInicio">Hora inicial</label>
        <input type="time" id="horaIni" name="horaIni" class="form-control" required>
        <br>
        <label for="fecha_final">Ingresa la fecha de finalización</label>
        <input type="date" id="final" name="final" class="form-control" required>
        <br>
        <label for="tiempoFinal">Hora final</label>
        <input type="time" id="horaFnl" name="horaFnl" class="form-control" required>
        <br><br>
        <button type="submit" id="enviarFormulario">Enviar</button>
        <br>
    </form>

    <!-- Bootstrap JavaScript -->
    <script src="bootstrap.js"></script>
    <!-- Other scripts and libraries -->
    <script src=".datatable/datatable.js"></script>
  </body>
</html>

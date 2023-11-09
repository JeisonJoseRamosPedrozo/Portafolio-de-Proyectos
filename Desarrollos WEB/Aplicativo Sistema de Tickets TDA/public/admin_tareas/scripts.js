      $(document).ready(function() {
        function validarFormulario() {
          var alerta = document.querySelector(".alert")
          var tarea = $("#tarea").val();
          var empleado = $("#empleado").val();
          var inicio = $("#inicio").val();
          var horaIni = $("#horaIni").val();
          var final = $("#final").val();
          var horaFnl = $("#horaFnl").val();
      
          // Realiza la validación de los campos
          if (tarea.trim() === "" || empleado.trim() === "" || inicio.trim() === "" || horaIni.trim() === "" || final.trim() === "" || horaFnl.trim() === "") {
            alerta.style.display="block";
            alerta.innerHTML=('<h3 class="alert-title">Campos vacios</h3><p class="alert-content">Ingresa campos validos</p>')
          }else{
          // El formulario es válido, se envía
          $("#formulario").submit();}
        }
      
        // Agrega el evento submit al formulario para llamar a la función de validación
        $("#action").on("click", function() {
          return validarFormulario();
        });
        $("#btnModificar").on("click", function() {
          return validarFormulario();
        });
        function modificarDatos(){
          
        }
        var tabla = $("#datos_usuario");

        // Configura el DataTable con el parámetro ajax
        var data_table = tabla.DataTable({
          ajax: {
            url: "obtenerRegistros.php",
            method: "GET",
            dataType: "json",
            dataSrc: "" // Si los datos se devuelven directamente como un arreglo, utiliza una cadena vacía para dataSrc
          },
          columns: [
            { data: "id_tarea" },
            { data: "tarea" },
            { data: "empleado" },
            { data: "fecha_inicio" },
            { data: "hora_inicial" },
            { data: "fecha_final" },
            { data: "hora_final" },
            { data: "estado" },
            {
              // Columna de acciones con los botones
              data: null,
              render: function(data, type, row) {
                return "<button type='button' class='btn btn-primary btn-modificar'><i class='bi bi-pencil-fill'></i> Modificar</button>" +
                       "<button type='button' class='btn btn-danger btn-eliminar'><i class='bi bi-trash-fill'></i> Eliminar</button>";
              }
            }
          ],
          columnDefs: [
            {
              // Alinear los botones al centro de la columna
              targets: -1,
              className: "text-center"
            }
          ]
        });
      });
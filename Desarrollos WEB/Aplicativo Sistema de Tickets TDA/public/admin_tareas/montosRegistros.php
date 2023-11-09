<?php
  include ("conexionSql.php");
  $query="SELECT * FROM pagos";
  $registros= $conn->query($query);
  $datos = $registros->fetchAll(PDO::FETCH_ASSOC);
  echo json_encode($datos);

?>
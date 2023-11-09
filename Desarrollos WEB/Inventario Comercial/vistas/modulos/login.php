<!DOCTYPE html>
<html>
<head>
  <title>Página de Inicio de Sesión</title>
  <style>
    .back {
      background-image: url('vistas/img/plantilla/portada.jpg');
      background-size: cover;
      background-repeat: no-repeat;
      background-position: center;
      width: 100%;
      height: 100vh;
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
    }
    .center-btn {
      display: flex;
      justify-content: center;
    }
    
    .login-btn {
      max-width: 200px;
      margin: 0 auto; 
      background-color: green; 
      color: white; 
      font-size: 0.9em;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      cursor: pointer;
    }
    .login-btn:hover {
      background-color: greenyellow;
      color: black;
    }
    
    input[type="text"],
    input[type="password"] {
      background-color: black; 
      color: white; 
      border: 1px solid gray; 
      border-radius: 5px;
      padding: 8px;
      font-size: 16px;
      width: 100%;
    }
  </style>
</head>
<body>

<div class="back"></div>

<div class="login-box">
  
  <div class="login-logo">
    <img src="vistas/img/plantilla/icono.png" class="img-responsive" style="padding:30px 100px 0px 100px">
  </div>

  <div class="login-box-body" style="background-color: white; color: black; font-size: 1.5em">

    <p class="login-box-msg">Ingresar al sistema</p>

    <form method="post">

      <div class="form-group has-feedback">

        <input type="text" class="form-control" placeholder="Usuario" name="ingUsuario" required>

      </div>

      <div class="form-group has-feedback">

        <input type="password" class="form-control" placeholder="Contraseña" name="ingPassword" required>
      
      </div>

      <div class="row">
        <div class="col-xs-12 center-btn"> 
          <button type="submit" class="login-btn">Ingresar</button>
        </div>
      </div>

      <?php
        $login = new ControladorUsuarios();
        $login -> ctrIngresoUsuario();
      ?>

    </form>

  </div>

</div>

</body>
</html>

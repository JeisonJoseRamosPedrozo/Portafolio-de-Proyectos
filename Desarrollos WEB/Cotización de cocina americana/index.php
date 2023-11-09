<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="img/logo.png" type="image/png">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background: linear-gradient(rgb(188, 199, 224), rgb(182, 233, 218), rgb(174, 216, 221));
            text-align: center;
        }

        h1 {
            font-size: 48px;
            margin-bottom: 30px;
			margin-top: 9%;
            text-align: center;
            color: rgb(15, 3, 87);
        }

        p {
            font-size: 36px;
            margin-bottom: 50px;
            color: rgb(0, 0, 0);
            font-weight: bold;
            text-align: center;
        }

		.btn-container {
            position: fixed;
            top: 0;
            right: 0; /* Cambia la posición a la derecha */
            left: 0;
            height: 14%;
            background: linear-gradient(90deg, rgb(45, 207, 186) 5%, rgb(100, 214, 176) 35%, rgb(102, 226, 235) 60%);
            z-index: 999;
            display: flex;
            align-items: center;
            justify-content: flex-end; /* Cambia la alineación a la derecha */
            padding: 0 20px;
        }

        .btn {
            background-color: #140350;
            color: #20d8e6;
            text-decoration: none;
            padding: 20px 50px;
            border-radius: 10px;
            font-size: 24px;
            transition: background-color 0.3s ease;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn:hover{
            background-color: #B2EBF2;
            color: black;
        }

        .btn-wrapper {
            display: flex;
            align-items: center;
        }

        .btn-container .btn {
            margin-left: 10px;
        }

        .btn-container .btn:first-child {
            margin-left: 0;
        }
        
        .btn-bordered {
            border: 2px solid #140350;
        }

        .btn-container2 {
            display: flex;
            flex-direction: row;
            align-items: stretch;
            justify-content: space-between;
            margin-top: 30px;
			margin-bottom: 50px;
			margin-left: 30px;
			margin-right: 30px;
            gap: 30px;
        }

        .btn2 {
            background: linear-gradient(90deg, rgb(10, 17, 78) 5%, rgb(15, 37, 163) 35%, rgb(51, 8, 100) 60%);
            color: #ffffff;
            text-decoration: none;
            padding: 20px;
            border-radius: 10px;
            font-size: 18px;
            transition: background-color 0.3s ease;
            text-align: center;
            width: calc((100% - 60px) / 3);
			margin: 2%;
        }

        .btn2:hover {
            background: linear-gradient(90deg, rgb(9, 64, 215) 5%, rgb(40, 13, 104) 35%, rgb(66, 0, 148) 60%);
			cursor: default;
        }

        @media (max-width: 768px) {
            h1 {
				margin-top: 22%;
                font-size: 36px;
            }

            p {
                font-size: 24px;
                margin-bottom: 30px;
            }

            .btn-container {
                position: fixed;
                top: 0;
                right: 0; /* Cambia la posición a la derecha */
                left: 0;
                height: 20%;
                background: linear-gradient(90deg, rgb(45, 207, 186) 5%, rgb(100, 214, 176) 35%, rgb(102, 226, 235) 60%);
                z-index: 999;
                display: flex;
                align-items: center;
                justify-content: flex-end; /* Cambia la alineación a la derecha */
                padding: 0 20px;
            }

            .btn {
                background-color: #140350;
                color: #20d8e6;
                text-decoration: none;
                border-radius: 10px;
                font-size: 0.77em;
                transition: background-color 0.3s ease;
                font-weight: bold;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .btn:hover{
                background-color: #B2EBF2;
                color: black;
            }

            .btn-wrapper {
                display: flex;
                align-items: center;
            }

            .btn-container .btn {
                margin-left: 10px;
            }

            .btn-container .btn:first-child {
                margin-left: 0;
            }

            .btn-container2 {
                display: flex;
				flex-direction: column;
				align-items: stretch;
				justify-content: space-between;
				margin-top: 30px;
				margin-bottom: 50px;
				margin-left: 33px;
            }

            .btn2 {
                font-size: 16px;
                width: 100%;
				margin: 0;
				width: calc((180% - 1px) / 2);
            }

        }
    </style>
    <title>Remodelando</title>
</head>

<body>
	<div class="btn-container">
		<div class="btn-wrapper">
			<a href="iniciosesion.php" class="btn btn-bordered">Iniciar sesión</a>
			<a href="registro.php" class="btn btn-bordered">Registrarse</a>
		</div>
	</div>
    <br><br><br>
    <h1>Bienvenido a nuestra firma constructora</h1>
    <p>Remodelamos tu cocina con los más altos estándares de calidad y diseño.</p>
    <br><br>
	<div>
    	<a href="registro.php" class="btn btn-bordered">Simular la remodelación de mi cocina</a>
	</div>
    <br><br>
    <div>
        <div class="btn-container2">
            <a class="btn2">Nos preocupamos por ofrecerte una experiencia personalizada desde el primer contacto. Nuestro equipo de expertos estará a tu disposición para entender tus necesidades y convertir tus sueños en realidad. ¡Tu satisfacción es nuestra prioridad!<br><br><br><img src="img/cocina1.jpg" alt="Imagen de cocina" style="max-width: 100%; height: auto;"></a>
            <a class="btn2">Nos especializamos en ofrecer diseños de cocina innovadores y personalizados que reflejen tu estilo y personalidad. Nuestro talentoso equipo de diseñadores trabajará estrechamente contigo para crear un espacio único y funcional que se adapte a tus necesidades y preferencias. Desde soluciones de almacenamiento inteligentes hasta detalles de diseño elegantes, transformaremos tu cocina en un verdadero centro de atención.<br><br><br><img src="img/cocina2.jpg" alt="Imagen de cocina" style="max-width: 100%; height: auto;"></a>
            <a class="btn2">Nos enorgullece ofrecer una garantía de satisfacción completa en todos nuestros proyectos de remodelación de cocinas. Estamos tan seguros de la calidad de nuestro trabajo que te ofrecemos la tranquilidad de saber que si algo no cumple con tus expectativas, lo solucionaremos sin costos adicionales. Tu felicidad es nuestra mayor recompensa.<br><br><br><img src="img/cocina3.jpg" alt="Imagen de cocina" style="max-width: 100%; height: auto;"></a>
        </div>
    </div>
</body>
</html>

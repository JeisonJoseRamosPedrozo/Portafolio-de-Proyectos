<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start(); // Iniciar la sesión si aún no está activa
}

require_once('library/tcpdf.php');

// Crea una nueva instancia de TCPDF
$pdf = new TCPDF('P', 'mm', 'A4', true, 'UTF-8');
$pdf->SetTitle('Cotizacion de la cocina');
$pdf->SetAuthor('Remodelando');
$pdf->SetSubject('Total de la cotizacion');
$pdf->SetKeywords('PDF, ejemplo, TCPDF');
$pdf->AddPage();

$css = '
    <style>
        h1 {
            color: #000066;
            text-align: center;
        }

        p {
            font-size: 1.3em;
            color: #000000; /* Color azul para el texto */
            text-align: center;
        }

        .image-container {
            text-align: center;
            margin-bottom: 20px;
        }

        .image-container img {
            max-width: 200px; /* Limita el ancho de la imagen a 200px */
        }
    </style>
';

// Consulta a la base de datos y construye el contenido HTML
$conn = mysqli_connect("localhost", "root", "", "cotizacion");
if ($conn) {
    $email = $_SESSION['correo'];
    $query = "SELECT names, email, numberphone, city, departament, direccion FROM usuarios WHERE email='$email'";
    $result = mysqli_query($conn, $query);

    if (mysqli_num_rows($result) > 0) {
        // Construye el contenido HTML con los resultados de la consulta
        $html1 = $css. '<h1>Datos del cliente:</h1><br><br><p>';
        while ($row = mysqli_fetch_assoc($result)) {
            $html1 .= "Nombre: " . $row['names'] . ".<br>";
            $html1 .= "Correo electrónico: " . $row['email'] . ".<br>";
            $html1 .= "Número de celular: " . $row['numberphone'] . ".<br>";
            $html1 .= "Ciudad de residencia: " . $row['city'] . ".<br>";
            $html1 .= "Departamento: " . $row['departament'] . ".<br>";
            $html1 .= "Direccion: " . $row['direccion'] . ".<br><br>";
        }
        $html1 .= '</p><br>';
    } else {
        $html1 = '<p>No se encontraron resultados.</p>';
    }

    mysqli_close($conn);
} else {
    $html1 = '<p>Error en la conexión a la base de datos</p>';
}

// Agrega el contenido HTML al documento PDF
$pdf->writeHTML($html1, true, false, true, false, '');


// Consulta a la base de datos y construye el contenido HTML
$conn = mysqli_connect("localhost", "root", "", "cotizacion");
if ($conn) {
    $email = $_SESSION['correo'];
    $query = "SELECT nevera, estufa, campana, lavavajillas, cotizacion FROM cotizar WHERE email= '$email'";
    $result = mysqli_query($conn, $query);

    if (mysqli_num_rows($result) > 0) {
        // Construye el contenido HTML con los resultados de la consulta
        $html2 = $css. '<h1>Total de la cotización:</h1><br><br><div class="image-container"><img src="img/imagencocina.jpg"></div><br><br><p>';
        while ($row = mysqli_fetch_assoc($result)) {
            $html2 .= "Nevera: " . $row['nevera'] . " pulgadas.<br>";
            $html2 .= "Estufa: " . $row['estufa'] . " pulgadas.<br>";
            $html2 .= "Campana extractora: " . $row['campana'] . " pulgadas.<br>";
            $html2 .= "Lavavajillas: " . $row['lavavajillas'] . " pulgadas.<br>";
            $html2 .= "Cotización: USD$" . $row['cotizacion'] . ".<br><br>";
        }
        $html2 .= '</p><br>';
    } else {
        $html2 = '<p>No se encontraron resultados.</p>';
    }
    
    mysqli_close($conn);
    } else {
        $html2 = '<p>Error en la conexión a la base de datos</p>';
    }
    
    // Agrega el contenido HTML al documento PDF
    $pdf->writeHTML($html2, true, false, true, false, '');
    
    // Cierra el documento PDF y genera la salida
    $pdf->Output('documento.pdf', 'I');
?>    
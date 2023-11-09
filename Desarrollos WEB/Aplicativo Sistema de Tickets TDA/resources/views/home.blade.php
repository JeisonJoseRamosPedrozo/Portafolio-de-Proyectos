@extends('layouts.app')
<link rel="icon" href="/svg/logoTDA2.png" type="png">
@section('content')

<link href="{{ asset('css/home.css') }}" rel="stylesheet">
<body>
    <br><br><br><br>
    <div class="card p-3" style="border: none; text-align: center; background-image: url('https://images.pexels.com/photos/13409734/pexels-photo-13409734.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1');">
        <div class="row">
            <div class="col-md-6">
                <h1 style= "color: white">Notificaciones</h1>
                <button type="button" class="btn btn-info">
                    <details>
                        <summary>
                            @if (Auth::check())
                                <?php
                                $user = Auth::user();
                                $email = $user->email;

                                // Obtener el número de identificación del usuario a partir del email
                                $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

                                // Realizar la consulta a tu tabla de base de datos usando el número de identificación
                                $consulta = DB::table('papelera_tareas')->where('identificacion', $identificacion)->get();

                                // Verificar si hay tareas asignadas
                                if ($consulta->isEmpty()) {
                                    echo '<img id="campana" src="/svg/campana.png" alt="Logo de la campana de notificaciones" style="width: 90px; height: 90px;" class="sin-tareas-asignadas">';
                                } else {
                                    echo '<img id="campana" src="/svg/campana.png" alt="Logo de la campana de notificaciones" style="width: 90px; height: 90px;" class="con-tareas-asignadas">';
                                }
                                ?>
                            @endif
                        </summary>
                        <p>
                            <div>
                                @if (Auth::check())
                                    <?php
                                    $user = Auth::user();
                                    $email = $user->email;

                                    // Obtener el número de identificación del usuario a partir del email
                                    $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

                                    session(['identificacion' => $identificacion]);

                                    // Realizar la consulta a tu tabla de base de datos usando el número de identificación
                                    $consulta = DB::table('papelera_tareas')->where('identificacion', $identificacion)->get();
                                    ?>
                                    
                                    @if ($consulta->isEmpty())
                                        <span class="no-tareas-asignadas">No tienes nuevos tickets asignados en este momento</span>
                                    @else
                                        <span class="tarea-asignada"><a href="{{ route('tareas.asignadas') }}" class="btn btn-active btn-large">Tienes tickets asignados en este momento</a></span>
                                    @endif
                                @endif
                            </div>
                        </p>
                    </details>
                </button>
            </div>
            <div class="col-md-6">
                <h1 style= "color: white">Tareas pendientes</h1>
                <button type="button" class="btn btn-info">
                    <details>
                        <summary>
                            @if (Auth::check())
                                <?php
                                $user = Auth::user();
                                $email = $user->email;

                                // Obtener el número de identificación del usuario a partir del email
                                $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

                                // Realizar la consulta a tu tabla de base de datos usando el número de identificación
                                $consulta = DB::table('pendientes')->where('identificacion', $identificacion)->get();

                                // Verificar si hay tareas asignadas
                                if ($consulta->isEmpty()) {
                                    echo '<img id="tarea" src="/svg/tarea.png" alt="Logo de las tareas pendientes" style="width: 90px; height: 90px;" class="sin-tareas-asignadas">';
                                } else {
                                    echo '<img id="tarea" src="/svg/tarea.png" alt="Logo de las tareas pendientes" style="width: 90px; height: 90px;" class="con-tareas-asignadas">';
                                }
                                ?>
                            @endif
                        </summary>
                        <p>
                            <div>
                                @if (Auth::check())
                                    <?php
                                    $user = Auth::user();
                                    $email = $user->email;

                                    // Obtener el número de identificación del usuario a partir del email
                                    $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

                                    session(['identificacion' => $identificacion]);

                                    // Realizar la consulta a tu tabla de base de datos usando el número de identificación
                                    $consulta = DB::table('pendientes')->where('identificacion', $identificacion)->get();
                                    ?>
                                    
                                    @if ($consulta->isEmpty())
                                        <span class="no-tareas-asignadas">No tienes tickets pendientes en este momento</span>
                                    @else
                                        <span class="tarea-asignada"><a href="{{ route('tareas.pendientes') }}" class="btn btn-active btn-large">Tienes tickets pendientes en este momento</a></span>
                                    @endif
                                @endif
                            </div>
                        </p>
                    </details>
                </button>
            </div>
        </div>
        <br><br><br>
        <div><h2 style= "color: white; font-size: 38px;">Pagos recibidos:</h2></div>
                <div>
                    @if (session('status'))
                        <div class="alert alert-success" role="alert">
                            {{ session('status') }}
                        </div>
                    @endif
                </div>
                <br>
                <div class="flex justify-center">
                <button type="button" class="btn btn-info">
                    <a href="{{ asset('users/pagosRecibidos.php?identificacion=' . $identificacion) }}" class="btn btn-active" style="font-size: 20px; font-weight: bold; color: black">Consultar</a>
                </button>
                </div>
        
        <div>
            @if (session('status'))
                <div class="alert alert-success" role="alert">
                    {{ session('status') }}
                </div>
            @endif
        </div>
    </div>

    <script>
        // Redirigir al usuario al intentar retroceder en la página
        window.addEventListener('popstate', function () {
            window.location = '{{ route('login') }}';
        });

        // Agregar una entrada en el historial del navegador para evitar la redirección al cargar la página
        history.pushState(null, null, location.href);
        window.onpopstate = function () {
            history.go(1);
        };
    </script>
</body>
@endsection

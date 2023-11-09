<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/svg/logoTDA2.png" type="png">
    <link href="{{ asset('css/welcome.css') }}" rel="stylesheet">
    <title>Empresa TDA</title>

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.bunny.net">
    <link href="https://fonts.bunny.net/css?family=figtree:400,600&display=swap" rel="stylesheet" />
</head>


<body class="antialiased">
<div class="relative flex flex-col justify-center items-center min-h-screen bg-dots-darker bg-center bg-gray-100 dark:bg-dots-lighter dark:bg-gray-900 selection:bg-red-500 selection:text-white">
    <div class="max-w-7xl mx-auto p-6 lg:p-8">
        <div class="flex justify-center">
            <h1 class="mb-4">Bienvenido a la app TDA</h1>
        </div>
        @if (Route::has('login'))
            <div class="flex justify-center">
                @auth
                    <a href="{{ url('/home') }}" class="btn btn-active">Activo</a>
                @else
                    <a href="{{ route('login') }}" class="btn">Iniciar Sesión</a>

                    @if (Route::has('register'))
                        <a href="{{ route('register') }}" class="btn">Registrarme</a>
                    @endif
                @endauth
            </div>
        @endif
    </div>
</div>
</body>
</html>

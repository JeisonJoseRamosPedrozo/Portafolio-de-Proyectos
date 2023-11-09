@extends('layouts.app')
<link rel="icon" href="/svg/logoTDA2.png" type="png">
@section('content')

<link href="{{ asset('css/dashboard.css') }}" rel="stylesheet">
<body>
    <div class="card p-3" style="border: none; text-align: center;background-color: #e0f1f9;">
        <div class="row" style="background-color: #e0f1f9;">
            <div class="col-md-6">
                <br><br><br>
                <div><h2>Administrador de Tareas:</h2></div>
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
                        <a href="{{ asset('admin_tareas/index_administrador.php') }}" class="btn btn-active">Administrar</a>
                    </button>
                </div>
            </div>
            <div class="col-md-6">
            <br><br><br>
                <div><h2>Registro de Integrantes:</h2></div>
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
                        <a href="{{ asset('admin_tareas/registro_trabajadores.php') }}" class="btn btn-active">Explorar</a>
                    </button>
                </div>
            </div>
            <div class="col-md-6">
            <br><br><br>
                <div><h2>Historial de Actividades:</h2></div>
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
                        <a href="{{ asset('admin_tareas/historial_actividades.php') }}" class="btn btn-active">Consultar</a>
                    </button>
                </div>
            </div>
            <div class="col-md-6">
            <br><br><br>
                <div><h2>Remunerar Trabajadores:</h2></div>
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
                        <a href="{{ asset('admin_tareas/remuneraciones.php') }}" class="btn btn-active">Realizar pagos</a>
                    </button>
                </div>
            </div>
        </div>
    </div>
    </div>
</body>
    
@endsection

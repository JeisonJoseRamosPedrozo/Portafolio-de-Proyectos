@extends('layouts.app')
<link rel="icon" href="/svg/logoTDA2.png" type="png">
@section('content')

<link rel="stylesheet" href="{{ url('/index.css') }}">
<body>
    <div class="card p-3" style="border: none; text-align: center;">
        <br><br><br>
        <div><h2>Estás activo y eres el Supervisor de TDA!</h2></div>
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
                <a href="{{ asset('admin_tareas/index_supervisor.php') }}" class="btn btn-active">Supervisor de Tareas</a>
            </button>
        </div>

    </div>
</body>
    

</div>
@endsection

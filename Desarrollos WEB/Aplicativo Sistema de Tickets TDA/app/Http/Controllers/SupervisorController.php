<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Tarea; // Importar el modelo Tarea si está definido

class SupervisorController extends Controller
{
    public function supervision()
    {
        return view('supervision');
    }

}

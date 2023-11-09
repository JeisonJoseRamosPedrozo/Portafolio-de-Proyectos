<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Tarea; // Importar el modelo Tarea si está definido

class AdminController extends Controller
{
    public function dashboard()
    {
        return view('dashboard');
    }

}

<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redirect;
use Illuminate\Support\Facades\Session;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\File;

class TareasController extends Controller
{
    public function mostrarTareasAsignadas()
    {
        if (Auth::check()) {
            $user = Auth::user();
            $email = $user->email;

            // Obtener el número de identificación del usuario a partir del email
            $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

            // Obtener la última tarea asignada al usuario
            $ultimaTarea = DB::table('tareas')->where('identificacion', $identificacion)->latest('id_tarea')->first();

            // Guardar el ID de la última tarea en la sesión
            Session::put('ultima_tarea_id', $ultimaTarea->id_tarea);

            // Realizar la consulta a tu tabla de base de datos usando el número de identificación
            $consulta = DB::table('tareas')->where('identificacion', $identificacion)->get();

            return view('asignadas', ['tareas' => $consulta]);
        }

        return redirect()->route('login')->with('error', 'El identificador de usuario no está definido en la sesión.');
    }


    public function decision(Request $request)
    {
        if (Auth::check()) {
            $user = Auth::user();
            $email = $user->email;

            // Obtener el número de identificación del usuario a partir del email
            $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

            $decision = $request->input('decision');

            // Obtener el ID de la última tarea almacenado en la variable de sesión
            $tareaId = Session::get('ultima_tarea_id');

            if ($decision == "aceptar") {
                DB::table('tareas')
                    ->where('identificacion', $identificacion)
                    ->where('id_tarea', $tareaId)
                    ->update([
                        'estado' => 'Aceptado'
                    ]);

                DB::table('historial')
                ->where('identificacion', $identificacion)
                ->where('id_tarea', $tareaId)
                ->update([
                    'estado' => 'Aceptado'
                ]);

                // Eliminar la tarea de la base de datos

                return redirect()->route('tareas.aceptadas');

            } elseif ($decision == "rechazar") {
                // Actualizar el campo de estado en la tabla de tareas

                DB::table('historial')
                ->where('identificacion', $identificacion)
                ->where('id_tarea', $tareaId)
                ->update([
                    'estado' => 'Rechazado',
                    'fecha_inicio' => null,
                    'hora_inicial' => null,
                    'fecha_final' => null,
                    'hora_final' => null,
                    'terminado' => 'No',
                    'conclusion' => 'No realizado'
                ]);

                // Eliminar la tarea de la base de datos
                DB::table('papelera_tareas')
                    ->where('identificacion', $identificacion)
                    ->where('id_tarea', $tareaId)
                    ->delete();

                DB::table('tareas')
                ->where('identificacion', $identificacion)
                ->where('id_tarea', $tareaId)
                ->delete();

                DB::table('pendientes')
                ->where('id_tarea', $tareaId)
                ->delete();
            }

            return redirect()->route('home');
        }

        return redirect()->route('login')->with('error', 'El identificador de usuario no está definido en la sesión.');
    }


    public function actualizar(Request $request)
    {
        if (Auth::check()) {
            $user = Auth::user();
            $email = $user->email;

            // Obtener el número de identificación del usuario a partir del email
            $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

            // Obtener la tarea a partir de la identificación y el ID de la tarea
            $tareaId = $request->input('tareaId');
            $tarea = DB::table('tareas')->where('identificacion', $identificacion)->where('id_tarea', $tareaId)->first();

            if ($tarea) {
                $fechaInicio = $request->input('inicio');
                $horaInicio = $request->input('horaIni');
                $fechaFinal = $request->input('final');
                $horaFinal = $request->input('horaFnl');

                // Actualizar los campos en la tabla de tareas
                DB::table('tareas')
                    ->where('id_tarea', $tareaId)
                    ->update([
                        'fecha_inicio' => $fechaInicio,
                        'hora_inicial' => $horaInicio,
                        'fecha_final' => $fechaFinal,
                        'hora_final' => $horaFinal
                    ]);

                DB::table('historial')
                ->where('id_tarea', $tareaId)
                ->update([
                    'fecha_inicio' => $fechaInicio,
                    'hora_inicial' => $horaInicio,
                    'fecha_final' => $fechaFinal,
                    'hora_final' => $horaFinal
                ]);

                DB::table('pendientes')
                ->where('id_tarea', $tareaId)
                ->update([
                    'identificacion' => $identificacion,
                    'fecha_inicio' => $fechaInicio,
                    'hora_inicial' => $horaInicio,
                    'fecha_final' => $fechaFinal,
                    'hora_final' => $horaFinal
                ]);

                // Eliminar la columna de la tabla de papelera_tareas que cumple ambas condiciones
                DB::table('papelera_tareas')
                    ->where('identificacion', $identificacion)
                    ->where('id_tarea', $tareaId)
                    ->delete();

                return redirect()->route('home');
            } else {
                // La tarea no existe para el usuario dado
                return redirect()->back()->with('error', 'La tarea no existe para el usuario actual.');
            }
        }

        return redirect()->route('login')->with('error', 'El identificador de usuario no está definido en la sesión.');
    }

    public function mostrarTareasPendientes()
    {
        if (Auth::check()) {
            $user = Auth::user();
            $email = $user->email;

            // Obtener el número de identificación del usuario a partir del email
            $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

            // Obtener la última tarea asignada al usuario
            $ultimaTarea = DB::table('tareas')->where('identificacion', $identificacion)->latest('id_tarea')->first();

            // Guardar el ID de la última tarea en la sesión
            Session::put('ultima_tarea_id', $ultimaTarea->id_tarea);

            // Realizar la consulta a tu tabla de base de datos usando el número de identificación
            $consulta = DB::table('tareas')->where('identificacion', $identificacion)->get();

            return view('pendientes', ['tareas' => $consulta]);
        }

        return redirect()->route('login')->with('error', 'El identificador de usuario no está definido en la sesión.');
    }

    public function evidencias(Request $request)
    {
        if (Auth::check()) {
            $user = Auth::user();
            $email = $user->email;

            // Obtener el número de identificación del usuario a partir del email
            $identificacion = DB::table('users')->where('email', $email)->value('identificacion');

            // Obtener el ID de la tarea desde el formulario
            $tareaId = $request->input('tareaId');

            // Obtener el archivo de evidencia subido por el usuario
            $evidenciaFile = $request->file('evidencia');

            if ($evidenciaFile) {
                // Validar que el archivo sea un PDF
                if ($evidenciaFile->isValid() && $evidenciaFile->getMimeType() === 'application/pdf') {
                    // Guardar el archivo en el disco "public"
                    $path = Storage::disk('public')->putFile('evidencias', $evidenciaFile);

                    // Actualizar la columna "evidencia" en la tabla "tareas"
                    DB::table('tareas')
                        ->where('identificacion', $identificacion)
                        ->where('id_tarea', $tareaId)
                        ->update([
                            'evidencia' => $path
                        ]);
                    
                    DB::table('historial')
                    ->where('identificacion', $identificacion)
                    ->where('id_tarea', $tareaId)
                    ->update([
                        'evidencia' => $path
                    ]);

                    return redirect()->route('home')->with('success', 'Evidencia entregada correctamente.');
                } else {
                    return redirect()->back()->with('error', 'El archivo seleccionado no es un PDF válido.');
                }
            }

            return redirect()->back()->with('error', 'No se seleccionó ningún archivo de evidencia.');
        }

        return redirect()->route('login')->with('error', 'El identificador de usuario no está definido en la sesión.');
    }
    
    public function eliminarPendiente(Request $request)
    {
        $tareaId = $request->input('tareaId');

        // Eliminar la tarea de la tabla 'pendientes'
        DB::table('pendientes')
            ->where('id_tarea', $tareaId)
            ->delete();


        DB::table('tareas')
        ->where('id_tarea', $tareaId)
        ->delete();


        DB::table('tareas')
        ->where('id_tarea', $tareaId)
        ->update([
            'terminado' => 'Si',
            'conclusion' => 'Finalizado'
        ]);

        DB::table('historial')
        ->where('id_tarea', $tareaId)
        ->update([
            'terminado' => 'Si',
            'conclusion' => 'Finalizado'
        ]);

        return response()->json(['success' => true]);
    }



}


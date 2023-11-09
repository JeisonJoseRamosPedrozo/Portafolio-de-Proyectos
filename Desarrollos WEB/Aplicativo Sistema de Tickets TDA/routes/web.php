<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AdminController; // Agregar esta línea
use App\Http\Controllers\TareasController; // Agregar esta línea

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Auth::routes();

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home');
Route::get('/admin/dashboard', [AdminController::class, 'dashboard'])->name('admin.dashboard')->middleware('auth');
Route::view('/supervision', 'supervision')->name('supervision')->middleware('auth');
Route::get('/tareas-asignadas', [TareasController::class, 'mostrarTareasAsignadas'])->name('tareas.asignadas')->middleware('auth');
Route::post('/tareas/decision', [TareasController::class, 'decision'])->name('tareas.decision')->middleware('auth');
Route::view('/aceptadas', 'aceptadas')->name('tareas.aceptadas')->middleware('auth');
Route::post('/actualizar-tareas', [TareasController::class, 'actualizar'])->name('tareas.actualizar');
Route::get('/tareas-pendientes', [TareasController::class, 'mostrarTareasPendientes'])->name('tareas.pendientes')->middleware('auth');
Route::post('/tareas/evidencias', [TareasController::class, 'evidencias'])->name('tareas.evidencias')->middleware('auth');
Route::post('/tareas/eliminar-pendiente', [TareasController::class, 'eliminarPendiente'])->name('tareas.eliminarPendiente');



<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use App\Models\User;

class AddIsSupervisorProfileToUsers extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('users', function (Blueprint $table) {
            $table->boolean('is_supervisor_profile')->default(false);
        });

        // Verificar si ya existe un supervisor
        $supervisor = User::where('is_supervisor_profile', true)->first();

        if (!$supervisor) {
            // Asignar 'true' al segundo usuario que se registre
            $secondUser = User::skip(1)->first();
            if ($secondUser) {
                $secondUser->is_supervisor_profile = true;
                $secondUser->save();
            } else {
                $customUser = new User();
                $customUser->name = 'Supervisor';
                $customUser->email = 'supervisor@example.com';
                $customUser->password = bcrypt('supervisorpassword');
                $customUser->is_supervisor_profile = true;
                $customUser->save();
            }
        }
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('users', function (Blueprint $table) {
            $table->dropColumn('is_supervisor_profile');
        });
    }
}

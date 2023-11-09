<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use App\Models\User;

class AddIsAdminToUsers extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('users', function (Blueprint $table) {
            $table->boolean('is_admin')->default(false);
        });

        // Asignar 'true' al primer usuario que se registre
        $firstUser = User::first();
        if ($firstUser) {
            $firstUser->is_admin = true;
            $firstUser->save();
        } else {
            $adminUser = new User();
            $adminUser->name = 'Admin';
            $adminUser->email = 'admin@example.com';
            $adminUser->password = bcrypt('adminpassword');
            $adminUser->is_admin = true;
            $adminUser->save();
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
            $table->dropColumn('is_admin');
        });
    }
}

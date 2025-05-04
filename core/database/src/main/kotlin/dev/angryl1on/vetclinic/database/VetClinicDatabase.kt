package dev.angryl1on.vetclinic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.angryl1on.vetclinic.database.dao.PetDao
import dev.angryl1on.vetclinic.database.table.PetTable

@Database(
    entities = [
        PetTable::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VetClinicDatabase : RoomDatabase() {

    abstract fun getPetDao(): PetDao

    fun clearDatabase() {
        clearAllTables()
    }
}

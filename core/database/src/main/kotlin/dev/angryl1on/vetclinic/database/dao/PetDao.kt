package dev.angryl1on.vetclinic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.angryl1on.vetclinic.database.table.PetTable
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao: BaseDao<PetTable> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pets: List<PetTable>)

    @Query("SELECT * FROM pets ORDER BY name")
    fun observeAll(): Flow<List<PetTable>>

    @Query("SELECT * FROM pets")
    suspend fun getPets(): List<PetTable>

    @Query("SELECT * FROM pets WHERE id = :id")
    suspend fun getPetById(id: Long): PetTable

    @Query("SELECT * FROM pets WHERE name = :name")
    suspend fun getPetByName(name: String): PetTable

    @Query("DELETE FROM pets")
    suspend fun deleteAll()

    @Query("DELETE FROM pets WHERE id = :id")
    suspend fun delete(id: Long)
}

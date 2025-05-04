package dev.angryl1on.vetclinic.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

/**
 * This contains common functions between all the dao
 */
interface BaseDao<T> {

    /**
     * Insert single value on database
     */
    @Insert(onConflict = REPLACE)
    suspend fun insert(value: T)

    /**
     * Updates Table with new data
     *
     * [value] is updated data
     */
    @Update(onConflict = REPLACE)
    suspend fun update(value: T)

    /**
     * Delete data from table
     */
    @Delete
    suspend fun delete(value: T)
}

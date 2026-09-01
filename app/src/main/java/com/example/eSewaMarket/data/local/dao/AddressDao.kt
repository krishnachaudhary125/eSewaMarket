package com.example.eSewaMarket.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.eSewaMarket.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddresses(
        addresses: List<AddressEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(
        address: AddressEntity
    )

    @Query("SELECT * FROM addresses WHERE userId = :userId")
    fun getAddresses(userId: Long): Flow<List<AddressEntity>>

    @Query("""
        SELECT * FROM addresses
        WHERE id = :addressId
        AND userId = :userId
    """
    )
    suspend fun getAddress(
        addressId: Long,
        userId: Long
    ): AddressEntity?

    @Query("DELETE FROM addresses WHERE userId = :userId")
    suspend fun clearAddress(userId: Long)

    @Query("DELETE FROM addresses WHERE userId = :userId AND id NOT IN (:serverIds)")
    suspend fun deleteNotInServer(userId: Long, serverIds: List<Long>)
}
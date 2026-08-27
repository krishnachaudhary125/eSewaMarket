package com.example.eSewaMarket.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import com.example.eSewaMarket.data.models.FavouriteResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favourite: FavouriteEntity)

    @Query("""
        DELETE FROM favourite WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun removeFromFavourite(userId: Long, productId: Long)

    @Query("""
        SELECT * FROM favourite WHERE userId = :userId AND productId = :productId LIMIT 1
    """
    )
    suspend fun getFavouriteItem(userId: Long, productId: Long): FavouriteEntity?

    @Query("SELECT COUNT(*) FROM favourite WHERE userId = :userId")
    fun getFavouriteCount(userId: Long): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite WHERE userId = :userId AND productId = :productId)")
    fun isFavourite(userId: Long, productId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favourites: List<FavouriteEntity>)

    @Query("""
        DELETE FROM favourite WHERE userId = :userId AND productId NOT IN (:serverProductIds)
    """
    )
    suspend fun deleteNotInServer(userId: Long, serverProductIds: List<Long>)

    @Query("""
        DELETE FROM favourite WHERE userId = :userId
    """
    )
    suspend fun clearFavourites(userId: Long)

    @Query("""
        SELECT productId
        FROM favourite
        WHERE userId = :userId
    """
    )
    suspend fun getFavouriteIds(userId: Long): List<Long>

    @Query("""
        SELECT p.productId, p.title, p.brand, p.price, p.thumbnail
        FROM favourite AS f
        INNER JOIN products AS p
        ON f.productId = p.productId
        WHERE f.userId = :userId
    """
    )
    fun getFavouriteProducts(userId: Long): Flow<List<FavouriteResponse>>
}
package com.example.eSewaMarket.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.eSewaMarket.data.local.entity.CartEntity
import com.example.eSewaMarket.data.models.ProductResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: CartEntity)

    @Query("""
        UPDATE cart SET quantity = :quantity WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun updateQuantity(userId: Long, productId: Long, quantity: Int)

    @Query("""
        DELETE FROM cart WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun removeFromCart(userId: Long, productId: Long)

    @Query("""
        SELECT * FROM cart WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun getCartItem(userId: Long, productId: Long): CartEntity?

    @Query("""
        SELECT quantity FROM cart WHERE userId = :userId AND productId = :productId
    """
    )
    fun getProductQuantity(userId: Long, productId: Long): Flow<Int>

    @Query("""
        SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE userId = :userId
    """
    )
    fun getTotalQuantity(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cartItems: List<CartEntity>)

    @Query("""
        DELETE FROM cart WHERE userId = :userId AND productId NOT IN (:serverProductIds)
    """
    )
    suspend fun deleteNotInServer(userId: Long, serverProductIds: List<Long>)

    @Query("""
        DELETE FROM cart WHERE userId = :userId
    """
    )
    suspend fun clearCart(userId: Long)

    @Query("""
        SELECT p.productId, p.title, p.brand, p.price, p.thumbnail, c.quantity
        FROM cart AS c
        INNER JOIN products AS p
        ON c.productId = p.productId
        WHERE c.userId = :userId
        ORDER BY c.addedAt DESC
    """
    )
    fun getCartProducts(userId: Long): Flow<List<ProductResponse>>

    @Query("""
    SELECT COALESCE(SUM(p.price * c.quantity), null)
    FROM cart AS c
    INNER JOIN products AS p
    ON c.productId = p.productId
    WHERE c.userId = :userId
    """
    )
    fun getTotalPrice(userId: Long): Flow<Double?>

    @Query("SELECT COUNT(*) FROM cart WHERE userId = :userId")
    fun getItemCount(userId: Long): Flow<Int>
}
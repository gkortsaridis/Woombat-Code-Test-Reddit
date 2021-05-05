package co.uk.gkortsaridis.woombatcodetest.utils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItemData

@Dao
interface RedditItemDataDAO {
    @Query("SELECT * FROM reddithotitemdata")
    fun getAll(): List<RedditHotItemData>

    @Insert
    fun insert(item: RedditHotItemData)

    @Query("SELECT * FROM reddithotitemdata WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): RedditHotItemData

    @Insert
    fun insertAll(items: List<RedditHotItemData>)

    @Delete
    fun delete(item: RedditHotItemData)

    @Query("DELETE FROM reddithotitemdata")
    fun nukeTable()
}
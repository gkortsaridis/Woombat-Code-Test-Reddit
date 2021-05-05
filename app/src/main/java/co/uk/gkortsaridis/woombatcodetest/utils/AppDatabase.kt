package co.uk.gkortsaridis.woombatcodetest.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItemData

@Database(entities = arrayOf(RedditHotItemData::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemsDao(): RedditItemDataDAO
}
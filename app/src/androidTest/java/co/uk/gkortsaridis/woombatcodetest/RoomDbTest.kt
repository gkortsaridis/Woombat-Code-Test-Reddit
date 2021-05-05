package co.uk.gkortsaridis.woombatcodetest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.uk.gkortsaridis.woombatcodetest.models.RedditHotItemData
import co.uk.gkortsaridis.woombatcodetest.utils.AppDatabase
import co.uk.gkortsaridis.woombatcodetest.utils.RedditItemDataDAO
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var dao: RedditItemDataDAO
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.itemsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeItemAndFindByTitle() {
        val title = "Custom title"
        val data  = RedditHotItemData(title = title, uid= 1)
        dao.insert(data)
        val byTitle = dao.findByTitle( title)
        Truth.assertThat(byTitle.title).isEqualTo(title)
    }
}
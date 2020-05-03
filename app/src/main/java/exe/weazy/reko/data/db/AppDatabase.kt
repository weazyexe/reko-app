package exe.weazy.reko.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import exe.weazy.reko.data.db.entities.MemeEntity

@Database(entities = [MemeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memesDao() : MemesDao
}
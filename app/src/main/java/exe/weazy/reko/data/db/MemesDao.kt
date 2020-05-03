package exe.weazy.reko.data.db

import androidx.room.*
import exe.weazy.reko.data.db.entities.MemeEntity
import io.reactivex.Observable

@Dao
interface MemesDao {

    @Query("SELECT * FROM memes")
    fun getAll(): Observable<List<MemeEntity>>

    @Query("SELECT * FROM memes WHERE id = :memeId")
    fun getById(memeId: Long) : Observable<MemeEntity>

    @Insert
    fun insert(meme: MemeEntity)

    @Update
    fun update(meme: MemeEntity)

    @Query("DELETE FROM memes")
    fun clearAll()
}
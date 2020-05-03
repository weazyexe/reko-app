package exe.weazy.reko.data

import exe.weazy.reko.data.db.MemesDao
import exe.weazy.reko.data.db.entities.MemeEntity
import exe.weazy.reko.data.network.NetworkService
import exe.weazy.reko.data.network.responses.MemesResponse
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Meme
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemesRepository {

    @Inject
    lateinit var memesDao: MemesDao

    @Inject
    lateinit var service: NetworkService

    lateinit var memes: MutableList<Meme>
    lateinit var localMemes: MutableList<Meme>

    private val mergeNetworkAndDb = BiFunction<List<MemesResponse>, List<MemeEntity>, List<Meme>> { response, entities ->
        val result = response.map { meme ->
            Meme(
                id = meme.id,
                title = meme.title,
                description = meme.description,
                isFavorite = entities.find { meme.id == it.id }?.isFavorite ?: meme.isFavorite,
                createDate = Date(meme.createDate),
                photoUrl = meme.photoUrl
            )
        } as MutableList<Meme>

        result.addAll(entities.map { it.convert() })

        return@BiFunction result.distinctBy { it.id }
    }

    private val getUserMemes = BiFunction<List<MemesResponse>, List<MemeEntity>, List<Meme>> { response, entities ->
        val fromNetwork = response.map { it.convert() }
        val fromLocal = entities.map { it.convert() }

        return@BiFunction fromLocal.filter { local -> fromNetwork.none { local.id == it.id } }
    }

    init {
        App.getComponent().injectMemesRepository(this)
    }

    fun fetch(): Observable<List<Meme>> = Observable.zip(
        service.fetchMemes(),
        memesDao.getAll(),
        mergeNetworkAndDb
    )

    fun fetchLocal(): Observable<List<Meme>> = Observable.zip(
        service.fetchMemes(),
        memesDao.getAll(),
        getUserMemes
    )

    fun getById(id: Long): Observable<Meme> = memesDao.getById(id)
        .map { it.convert() }

    fun save(meme: Meme) {
        this.memes.add(meme)
        this.localMemes.add(meme)

        memesDao.insert(meme.toEntity())
    }

    fun save(memes: List<Meme>) {
        this.memes = memes as MutableList<Meme>
        memesDao.clearAll()

        memes.forEach {
            memesDao.insert(it.toEntity())
        }
    }

    fun like(meme: Meme) {
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            !meme.isFavorite,
            meme.createDate,
            meme.photoUrl
        )

        if (::memes.isInitialized) {
            val index = memes.indexOf(meme)

            if (index != -1) {
                memes[index] = updatedMeme
            }
        }

        if (::localMemes.isInitialized) {
            val index = localMemes.indexOf(meme)

            if (index != -1) {
                localMemes[index] = updatedMeme
            }
        }


        memesDao.update(updatedMeme.toEntity())
    }
}
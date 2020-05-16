package exe.weazy.reko.data

import com.google.firebase.Timestamp
import exe.weazy.reko.data.firebase.RxFirebase
import exe.weazy.reko.data.firebase.raws.EmotionRaw
import exe.weazy.reko.data.firebase.raws.RecognizedRaw
import exe.weazy.reko.model.Recognized
import io.reactivex.Observable

class RecognizedRepository {

    val recognized: MutableList<Recognized> = mutableListOf()

    fun fetch(): Observable<List<Recognized>> = RxFirebase.getCollection("info/recognized")
        .map { response -> response.map { it.toObject(RecognizedRaw::class.java) } }
        .map { response -> response.filterNotNull() }
        .map { response ->
            val converted = response.map { it.convert() }
            recognized.addAll(converted)
            converted
        }

    fun save(data: Recognized): Observable<Recognized> = Observable.just(data)
        .map {
            this.recognized.add(data)
            RecognizedRaw(
                id = it.id,
                image = it.image,
                recognize_time = Timestamp(it.date),
                recognizer_name = it.recognizer.name,
                emotions = it.emotions.map { emotion -> EmotionRaw(emotion.key, emotion.value) }
            )
        }
        .flatMap {
            RxFirebase
                .saveDocument("info/recognized/${data.id}", it)
                .map { data }
        }
}